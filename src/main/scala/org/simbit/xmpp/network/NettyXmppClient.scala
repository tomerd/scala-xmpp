package org.simbit.xmpp
{
	package network
	{
		import java.net.InetSocketAddress
		import java.net.URI
		import java.util.concurrent.Executors
		import java.util.Date
		
		import javax.net.ssl.SSLEngine
		
		import scala.collection._
		
		import org.jboss.netty.bootstrap.ClientBootstrap
		import org.jboss.netty.channel._
		import org.jboss.netty.channel.Channels._
		import org.jboss.netty.channel.ChannelPipeline
		import org.jboss.netty.channel.ChannelPipelineFactory
		import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory
		//import org.jboss.netty.example.securechat.SecureChatSslContextFactory
		//import org.jboss.netty.handler.codec.http.HttpContentDecompressor
		import org.jboss.netty.handler.ssl.SslHandler
		
		import org.simbit.xmpp.protocol._
		
		import org.simbit.xmpp.util._
		
		object NettyXmppClient
		{
			// TODO: these should come from a configuration file
			val DEFAULT_TIMEOUT = 5 * 60 * 1000 
			val KEEPALIVE_INTERVAL = 60 * 1000
			val CLEANUP_INTERVAL = 5 * 60 * 1000
			val MAX_RECONNECT_ATTEMPTS = 5
		}
		
		abstract class NettyXmppClient extends NettyHandler[Packet] with Logger
		{
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
			
			private var _shuttingdown = false
			private var _reconnectAttampts = 0
			
			private var _lastActive:Long = 0
			
			protected def identifier:String
			
			final protected def connect(host:String, port:Int, ssl:Boolean=false, timeout:Int=NettyXmppClient.DEFAULT_TIMEOUT)
			{
				_host = host
				_port = port
				_ssl = ssl
				_timeout = timeout
				
				_bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool, Executors.newCachedThreadPool))
				_bootstrap.setPipelineFactory(new XmppClientPipelineFactory(this, ssl))
				
				// start the connection attempt.
				val future = _bootstrap.connect(new InetSocketAddress(host, port))				
				// wait until the connection attempt succeeds or fails.
				_channel = future.awaitUninterruptibly.getChannel
								
				if (!future.isSuccess) 
				{
					error(this.identifier + " connect error " + future.getCause) 
					disconnect
					return
				}
				
				// this is rquried because sometimes the channel signals that it is open before the connect future returns 
				flushQueue
								
				ScheduledJobsManager.registerJob("keeplalive_" + this.identifier, this.keepalive, NettyXmppClient.KEEPALIVE_INTERVAL)
				ScheduledJobsManager.registerJob("cleanup_" + this.identifier, this.cleanup, NettyXmppClient.CLEANUP_INTERVAL)
				ScheduledJobsManager.startAll
			}
			
			final protected def disconnect
			{
				try
				{
					_shuttingdown = true
					
					ScheduledJobsManager.stopAll
					
					if (null != _channel) _channel.getCloseFuture.awaitUninterruptibly
					/*{
						_channel.getCloseFuture.awaitUninterruptibly
						_channel.close
					}*/
					
					if (null != _bootstrap) _bootstrap.releaseExternalResources
				}
				catch
				{
					// TODO: do something intelligent here
					case e:Exception => error(this.identifier + " disconnect error " + e) 
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
					touch
					_channel.write(packet)
				}
				catch
				{
					// TODO: do something intelligent here
					case e:Exception => error(this.identifier + " failed sending packet to xmpp server")
				}
			}
			
			private def flushQueue
			{
				if (null == _channel) return
				if (0 == sendQueue.length) return
				
				sendQueue.foreach( item => send(item) )
				sendQueue.clear
			}
			
			protected def handleConnected
			{
				_reconnectAttampts = 0
				info(this.identifier + " is now connected")
				
				flushQueue
			}
			
			protected def handleDisconnected
			{
				if (_shuttingdown) return
				if (_reconnectAttampts < NettyXmppClient.MAX_RECONNECT_ATTEMPTS)
				{
					error(this.identifier + " was disconnected, attmepting to reconnect (attempt #" + (_reconnectAttampts+1) + ")")
					_reconnectAttampts = _reconnectAttampts+1
					connect(_host, _port, _ssl, _timeout)
				}
				else
				{
					error(this.identifier + " was disconnected " + NettyXmppClient.MAX_RECONNECT_ATTEMPTS + " times in a row, shutting down")
				}
			}
			
			protected def handle(packet:Packet)
			{
				touch
			}
			
			protected def handleProtocolError
			{
				//TODO: do something more intelligent here
				error(this.identifier + " protocol error")
			}
			
			protected def handleCommunicationException(e:Throwable)
			{
				//TODO: do something more intelligent here
				error(this.identifier + " communication exception " + e)
			}
			
			private def touch
			{
				_lastActive = new Date().getTime
			}
			
			private def keepalive()
			{
				// dont over do it
				if (new Date().getTime - _lastActive < NettyXmppClient.KEEPALIVE_INTERVAL/2) return
				send(" ")
			}
			
			// to be implemented by sub classes as required
			protected def cleanup()
			{
			}
			
		}
		
		private class XmppClientPipelineFactory(handler:ChannelUpstreamHandler, ssl:Boolean) extends ChannelPipelineFactory 
		{
			def getPipeline():ChannelPipeline =
			{
				// Create a default pipeline implementation.
				val pipe = pipeline()
				
				//FIXME: implement this
				/*
				if (ssl) 
									
					val engine = SecureChatSslContextFactory.getClientContext.createSSLEngine
					engine.setUseClientMode(true)
					
					pipe.addLast("ssl", new SslHandler(engine))
				}
				*/
				
				pipe.addLast("codec", XmppCodec.getCodec)
				
				// Remove the following line if you don't want automatic content decompression.
				//pipe.addLast("inflater", new HttpContentDecompressor)
				
				// Uncomment the following line if you don't want to handle HttpChunks.
				//pipe.addLast("aggregator", new HttpChunkAggregator(1048576))
				
				pipe.addLast("handler", handler)
				
				return pipe
			}
		}
		
		
	}
}
