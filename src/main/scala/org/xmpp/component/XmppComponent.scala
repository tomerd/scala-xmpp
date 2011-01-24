package org.xmpp
{
	package component
	{
		import java.io._
		import java.util._
		
		import java.io.IOException
		
		import java.net.Socket
		import java.net.InetSocketAddress
		
		import scala.xml._
		
		// TODO: replace all dom4j and xmlpull with native scala xml
		//import org.xmlpull.v1.XmlPullParser
		//import org.xmlpull.v1.XmlPullParserException
		//import org.xmlpull.v1.XmlPullParserFactory
		
		// TODO: replace all dom4j and xmlpull with native scala xml
		//import org.dom4j.Element	
		//import org.dom4j.io.XPPPacketReader
		
		import net.lag.naggati.IoHandlerActorAdapter
		import net.lag.naggati.MinaMessage
		import net.lag.naggati.ProtocolError
					
		import org.apache.mina.core.session.IoSession
		//import org.apache.mina.core.buffer.IoBuffer
		import org.apache.mina.filter.codec.ProtocolCodecFilter
		//import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory
		import org.apache.mina.filter.logging.LoggingFilter
		import org.apache.mina.transport.socket.nio.NioSocketConnector
		//import org.apache.mina.common.ConnectFuture
		
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
					send("</stream:stream>")
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
							send("<stream:stream xmlns=\"jabber:component:accept\" xmlns:stream=\"http://etherx.jabber.org/streams\" to=\"" + domain + "\">")							
						}
						case MinaMessage.MessageReceived(msg) => 
						{
							handle(msg.toString)
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
			
  			private def handle(message:String) = 
			{				
				// TODO: use pattern matching here
				if (message.indexOf("<stream:stream xmlns:stream='http://etherx.jabber.org/streams' xmlns='jabber:component:accept'") >= 0)
				{
					try
					{
						// stream open succeeded, now hanshake
						val xml = XML.loadString(message + "</stream:stream>")
						val connectionId = (xml \ "@id").text
						send(Handshake(connectionId, secret))
					}
					catch
					{
						// TODO, do something more intelligent here
						case e:Exception => println("handhsake error " + e)
					}
				}
				else if (message.indexOf("<handshake/>") >= 0)
				{
					// handlshake succeeded
					// TODO, do something more intelligent here
					println("connected to xmpp server")
				}
				else if (message.indexOf("<error") >= 0)
				{
					// TODO, do something more intelligent here
					println("stream error " + message)
				}
				else
				{
					try
					{
						val stanza = Stanza(message)
						stanzaHandler(stanza)
					}
					catch
					{
						// TODO, do something more intelligent here
						case e:Exception => println("error parsing or handling stanza " + e + "\n" + message)
					}
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