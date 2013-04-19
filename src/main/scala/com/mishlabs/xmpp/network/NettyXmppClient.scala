package com.mishlabs.xmpp
package network

import java.net.InetSocketAddress
import java.util.concurrent.Executors

import scala.collection._

import org.jboss.netty.bootstrap.ClientBootstrap
import org.jboss.netty.channel._
import org.jboss.netty.channel.ChannelPipelineFactory
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory

import com.mishlabs.xmpp.protocol._

import com.mishlabs.xmpp.util._

trait NettyXmppClientConfig
{
    val timeout:Int
    val maxReconnectAttempts:Int
    val keepAliveInterval:Int
    val cleanupInterval:Int
}

trait NettyXmppClient extends NettyClientConnection[Packet]
{
    protected val config:NettyXmppClientConfig

    private val logger = new Logger {}

    private var _bootstrap:ClientBootstrap = null
    private var _channel:Channel = null
    private val sendQueue = mutable.ListBuffer[AnyRef]()

    private var _host:String = null
    final protected def host = _host

    private var _port:Int = 0
    final protected def port = _port

    private var _ssl:Boolean = false
    final protected def ssl = _ssl

    private var _timeout:Int = 0
    final override protected def timeout = if (_timeout > 0) Some(_timeout) else None

    private var _shuttingDown = false
    private var _reconnectAttempts = 0

    private var _lastActive:Long = 0

    protected def identifier:String

    final protected def connect(host:String, port:Int, ssl:Boolean=false, timeout:Int=config.timeout)
    {
        _host = host
        _port = port
        _ssl = ssl
        _timeout = timeout

        val pipelineFactory = new ChannelPipelineFactory()
        {
            def getPipeline =
            {
                val codec = XmppCodec()
                //FIXME: implement this
                /*
                if (ssl)

                    val engine = SecureChatSslContextFactory.getClientContext.createSSLEngine
                    engine.setUseClientMode(true)

                   val ssl =  new SslHandler(engine))
                }
                */
                Channels.pipeline(codec, NettyXmppClient.this)
            }
        }

        _bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool, Executors.newCachedThreadPool))
        _bootstrap.setPipelineFactory(pipelineFactory)

        // start the connection attempt.
        val future = _bootstrap.connect(new InetSocketAddress(host, port))
        // wait until the connection attempt succeeds or fails.
        _channel = future.awaitUninterruptibly.getChannel

        if (!future.isSuccess)
        {
            logger.error(this.identifier + " connect error " + future.getCause)
            disconnect()
            return
        }

        // this is required because sometimes the channel signals that it is open before the connect future returns
        flushQueue()

        ScheduledJobsManager.registerJob("keeplalive_" + this.identifier, this.keepAlive, config.keepAliveInterval)
        ScheduledJobsManager.registerJob("cleanup_" + this.identifier, this.cleanup, config.cleanupInterval)
        ScheduledJobsManager.startAll
    }

    final protected def disconnect()
    {
        try
        {
            _shuttingDown = true

            ScheduledJobsManager.stopAll

            if (null != _channel) _channel.getCloseFuture.awaitUninterruptibly
            /*{
                _channel.getCloseFuture.awaitUninterruptibly
                _channel.close
            }*/

            if (null != _bootstrap) _bootstrap.releaseExternalResources()
        }
        catch
        {
            // TODO: do something intelligent here
            case e => logger.error(this.identifier + " disconnect error " + e)
        }
        finally
        {
            _channel = null
            _bootstrap = null
        }
    }

    final protected def send(packet:AnyRef)
    {
        if (null == _channel)
        {
            sendQueue += packet
            return
        }

        try
        {
            touch()
            _channel.write(packet)
        }
        catch
        {
            // TODO: do something intelligent here
            case e => logger.error(this.identifier + " failed sending packet to xmpp server")
        }
    }

    private def flushQueue()
    {
        if (null == _channel) return
        if (0 == sendQueue.length) return

        sendQueue.foreach( item => send(item) )
        sendQueue.clear()
    }

    protected def handleConnected()
    {
        _reconnectAttempts = 0
        logger.info(this.identifier + " is now connected")

        flushQueue()
    }

    protected def handleDisconnected()
    {
        if (_shuttingDown) return
        if (_reconnectAttempts < config.maxReconnectAttempts)
        {
            logger.error(this.identifier + " was disconnected, attempting to reconnect (attempt #" + (_reconnectAttempts+1) + ")")
            _reconnectAttempts += 1
            connect(_host, _port, _ssl, _timeout)
        }
        else
        {
            logger.error(this.identifier + " was disconnected " + config.maxReconnectAttempts + " times in a row, shutting down")
        }
    }

    protected def handle(packet:Packet)
    {
        touch()
    }

    protected def handleProtocolError
    {
        //TODO: do something more intelligent here
        logger.error(this.identifier + " protocol error")
    }

    protected def handleCommunicationException(e:Throwable)
    {
        //TODO: do something more intelligent here
        logger.error(this.identifier + " communication exception " + e)
    }

    private def touch()
    {
        _lastActive = System.currentTimeMillis
    }

    private def keepAlive()
    {
        // don't over do it
        if (System.currentTimeMillis - _lastActive < config.keepAliveInterval/2) return
        send(" ")
    }

    protected def cleanup() = Unit

}
