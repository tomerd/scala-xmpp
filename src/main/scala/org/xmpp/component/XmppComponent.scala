package org.xmpp
{
	package component
	{
		import java.io._
		import java.util._
		
		import java.io.IOException
		
		import java.net.Socket
		import java.net.InetSocketAddress
		
		import scala.collection._
		import scala.xml._
		
		import net.lag.naggati.IoHandlerActorAdapter
		import net.lag.naggati.MinaMessage
		import net.lag.naggati.ProtocolError
					
		import org.apache.mina.core.session.IoSession
		import org.apache.mina.filter.codec.ProtocolCodecFilter
		import org.apache.mina.filter.logging.LoggingFilter
		import org.apache.mina.transport.socket.nio.NioSocketConnector
		
		//import scala.actors.Actor
		//import scala.actors.Actor._
		import com.twitter.actors.Actor
		import com.twitter.actors.Actor._
		
		import org.xmpp.codec._
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		import org.xmpp.util._
		
		object XmppComponent
		{
			val DEFAULT_TIMEOUT = 5 * 1000
		}
		
		trait XmppComponent 
		{
			// getters
			private var _subdomain:String = _
			def subdomain:String = _subdomain
			
			private var _host:String = _
			def host:String = _host
			
			private var _port:Int = _
			def port:Int = _port
			
			private var _secret:String = _
			def secret:String = _secret
			
			private var _timeout:Int = _
			def timeout:Int = _timeout
						
			private var _connector:NioSocketConnector = _
			private var _session:IoSession = _
			
			def configure(subdomain:String, host:String, port:Int, secret:String, timeout:Int=0)
			{
				_subdomain = subdomain
				_host = host
				_port = port
				_secret = secret
				_timeout = if (null == timeout) timeout else XmppComponent.DEFAULT_TIMEOUT
				
				/*
				if (manager.getServerName != null) {
					this.domain = subdomain + "." + manager.getServerName
					}
					else {
					this.domain = subdomain
					}
				*/
				
				require(this.subdomain != null)
				require(this.host != null)
				require(this.port != 0)
				require(this.secret != null)
				require(this.timeout != 0)
			}
			
			def start
			{
				start(this.subdomain, this.host, this.port, this.secret, this.timeout)
			}
			
			def start(subdomain:String, host:String, port:Int, secret:String, timeout:Int=0)
			{
				configure(subdomain, host, port, secret, timeout)
				
				// TODO: look into this
				val domain = subdomain
								
				// start a mina thread pool listening on server messages
				_connector = new NioSocketConnector()
				_connector.setConnectTimeout(timeout)
				_connector.getFilterChain.addLast("codec", new ProtocolCodecFilter(Codec.encoder, Codec.decoder))
				_connector.getFilterChain.addLast("logger", new LoggingFilter())
				_connector.setHandler(new IoHandlerActorAdapter(session => new XmppHandler(session, domain, this.secret, this.handleStanza)))
				
				val future = _connector.connect(new InetSocketAddress(host, port))
				future.awaitUninterruptibly
				_session = future.getSession
				
				// keep alive
				//ScheduledJobsManager.registerJob("heartbeat", new HeartbeatJob(this.send, 60 * 1000))

				// cleanup job
				//ScheduledJobsManager.registerJob("cleanup", new CleanupJob(60 * 1000))
				//timeoutTask = new TimeoutTask
				//TaskEngine.getInstance.scheduleAtFixedRate(timeoutTask, 2000, 2000)
				
				//ScheduledJobsManager.startAll
					
				// TODO: add hook for implementations
			}
			
			def shutdown
			{
				try
				{
					send(StreamTail())
					if (null != _session) _session.getCloseFuture.awaitUninterruptibly
					if (null != _connector) _connector.dispose
					
					//_handler ! Disconnect
					ScheduledJobsManager.stopAll
				}
				catch
				{
					case e:Exception => println("disconnect error " + e) // TODO: do something intelligent here
				}
				finally
				{
					_session = null
					_connector = null
				}
				
				// TODO: add hook for implementations
			}
			
			def send(content:String) 
			{
				if (null == _session) return
				
				try 
				{
					_session.write(content)
				}
				catch
				{
					case e:IOException =>
					{
						// TODO: do something here
						println(e)
						//error(e)
						//if (!shutdown) reconnect
					}
				}
			}
			
			protected def handleStanza(stanza:Stanza)
		}
				
		private class XmppHandler(val session:IoSession, val domain:String, val secret:String, val stanzaHandler:(Stanza) => Unit) extends Actor 
		{
			//session.getConfig.setReadBufferSize(2048)
			IoHandlerActorAdapter.filter(session) -= classOf[MinaMessage.MessageSent]
			
			start
			
			def act = 
			{
				loop 
				{
					react 
					{	
						case MinaMessage.SessionOpened => 
						{
							// open xmpp stream
							val head = StreamHead("jabber:component:accept", immutable.List("to" -> domain))
							send(head)
						}
						case MinaMessage.MessageReceived(message) => 
						{
							handle(message)
						}
						case MinaMessage.SessionClosed => 
						{
							exit
						}
						case MinaMessage.SessionIdle(status) => 
						{
							session.close
						}
						case MinaMessage.ExceptionCaught(cause) => 
						{
							cause.getCause match 
							{
								// TODO, do something more intelligent here
								case e:Exception => println("mina exception caught: " + e)
							}
							session.close
						}
					}
				}
			}
			
  			private def handle(message:AnyRef) = 
			{
				message match
				{
  					case head:StreamHead =>
					{
						head.findAttribute("id") match
						{
							case Some(connectionId) => send(Handshake(connectionId, secret))
							// TODO, do something more intelligent here
							case None => println("invaild stream head, conection id not found")
						}
					}
					// TODO, do something more intelligent here?
					case tail:StreamTail => println("tail")
					// TODO, do something more intelligent here?
					case handshake:Handshake => println("connected to xmpp server")
					// TODO, do something more intelligent here
					case error:StreamError => println("stream error " + error)			
					case stanza:Stanza => handleStanza(stanza)
					case stanzas:Seq[Stanza] => stanzas.foreach( stanza => handleStanza(stanza) )	
				}
			}
  			
  			private def handleStanza(stanza:Stanza)
  			{
  				println("stanza recieved")
						
				try
				{
					stanzaHandler(stanza)
				}
				catch
				{
					// TODO, do something more intelligent here
					case e:Exception => println("error handling stanza " + e + "\n" + stanza)
				}
  			}
			
			private def send(content:String) = 
			{
				session.write(content)
			}
		}
		
		private class HeartbeatJob(sendHandler:(String) => Unit, interval:Int) extends ScheduledJob(interval)
		{
			def doJob
			{
				sendHandler(" ")
			}
		}
		
		private class CleanupJob(interval:Int) extends ScheduledJob(interval)
		{
			def doJob
			{
				
			}
		}

		
	}
}