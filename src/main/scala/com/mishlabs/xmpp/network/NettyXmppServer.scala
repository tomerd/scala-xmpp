package com.mishlabs.xmpp
package network

import java.security.{KeyStore, SecureRandom}

import java.net.InetSocketAddress
import java.util.concurrent.{Executors, ExecutorService, TimeUnit}

import javax.net.ssl.{SSLContext, KeyManagerFactory}

import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.channel._
import org.jboss.netty.channel.group.ChannelGroup
import org.jboss.netty.channel.group.DefaultChannelGroup
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import org.jboss.netty.util.HashedWheelTimer

import protocol.Packet

import com.mishlabs.xmpp.util.Logger
import org.jboss.netty.handler.ssl.SslHandler

trait NettyXmppServerConfig
{
    val clientTimeout:Int
}

trait NettyXmppServer[K]
{
    protected val config:NettyXmppServerConfig

    private var executor:ExecutorService = null
    private var channelFactory:ChannelFactory = null
    private var channelGroup:ChannelGroup = null
    private var inputAcceptor:Channel = null
    private var timer:HashedWheelTimer = null

    def startup(host:String, port:Int, keystore:KeyStore, keystorePassword:String)
    {
        // ideally would manage the ssl context in a lazy val, but scala throws an AbstractMethodError
        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
        kmf.init(keystore, keystorePassword.toCharArray)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(kmf.getKeyManagers, null, new SecureRandom())

        executor = Executors.newCachedThreadPool()
        channelFactory = new NioServerSocketChannelFactory(executor, executor)
        channelGroup = new DefaultChannelGroup("channels")
        timer = new HashedWheelTimer(10, TimeUnit.MILLISECONDS)

        val pipelineFactory = new ChannelPipelineFactory()
        {
            def getPipeline =
            {
                val codec = XmppCodec()
                val delegate = new ClientConnection(channelGroup, timer, config.clientTimeout, sslContext)
                Channels.pipeline(codec, delegate)
            }
        }

        val address = new InetSocketAddress(host, port)
        val bootstrap = new ServerBootstrap(channelFactory)
        bootstrap.setPipelineFactory(pipelineFactory)
        bootstrap.setOption("backlog", 1000)
        bootstrap.setOption("reuseAddress", true)
        bootstrap.setOption("child.keepAlive", true)
        bootstrap.setOption("child.tcpNoDelay", true)
        inputAcceptor = bootstrap.bind(address)
    }

    def shutdown()
    {
        if (null != timer) timer.stop()
        if (null != inputAcceptor) inputAcceptor.close.awaitUninterruptibly()
        if (null != channelGroup) channelGroup.close.awaitUninterruptibly()
        if (null != channelFactory) channelFactory.releaseExternalResources()

        if (null != executor)
        {
            executor.shutdown()
            executor.awaitTermination(5, TimeUnit.SECONDS)
        }
    }

    // subclass delegates
    protected def generateNewConnectionId():K
    protected def handleNewConnection(connectionId:K, connection:NettyXmppServerClientConnection)
    protected def handleDisconnect(connectionId:K)
    protected def handlePacket(connectionId:K, packet:Packet)
    protected def handleProtocolError(connectionId:K, e:Throwable)
    protected def handleCommunicationError(connectionId:K, e:Throwable)
    protected def handleSendError(connectionId:K, e:Throwable)

    protected trait NettyXmppServerClientConnection
    {
        def send(packet:Packet)
        def secure()
        def close()
    }

    private class ClientConnection(channelGroup:ChannelGroup, timer:HashedWheelTimer, timeout:Int, sslContext:SSLContext) extends NettyServerClientConnection[Packet](channelGroup, timer, Some(timeout)) with NettyXmppServerClientConnection
    {
        var id:Option[K] = None

        private val logger = new Logger()

        def secure()
        {
            this.channel.map( channel =>
            {
                if (null == channel.getPipeline.get("ssl"))
                {
                    logger.trace("securing netty client connection %s".format(id.get))
                    val engine = sslContext.createSSLEngine
                    engine.setUseClientMode(false)
                    //engine.setEnabledCipherSuites(Array[String]("TLS_RSA_WITH_AES_128_CBC_SHA"))
                    channel.getPipeline.addFirst("ssl", new SslHandler(engine))
                }
                else
                {
                    logger.warn("netty client connection %s already secured".format(id.get))
                }
            })
        }

        def close()
        {
            this.channel.map( _.close() )
        }

        protected def handleConnected()
        {
            id = Some(NettyXmppServer.this.generateNewConnectionId())
            logger.trace("new netty client connection %s".format(id.get))
            NettyXmppServer.this.handleNewConnection(id.get, this)
        }

        protected def handleDisconnected() { NettyXmppServer.this.handleDisconnect(id.get) }
        protected def handleMessage(packet:Packet) { NettyXmppServer.this.handlePacket(id.get, packet) }
        protected def handleProtocolError(e:Throwable) { NettyXmppServer.this.handleProtocolError(id.get, e) }
        protected def handleCommunicationError(e:Throwable) { NettyXmppServer.this.handleCommunicationError(id.get, e) }
        protected def handleSendError(e:Throwable) { NettyXmppServer.this.handleSendError(id.get, e) }
    }
}




