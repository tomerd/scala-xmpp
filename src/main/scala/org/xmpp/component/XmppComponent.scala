package org.xmpp
{
	package component
	{
		import java.io._
		import java.util._

		import java.io.IOException

		import java.net.Socket
		import java.net.InetSocketAddress
	
		//import java.util.concurrent.Executors
		//import java.util.concurrent.ExecutorService
		
		import scala.xml._
		
		// TODO: replace all dom4j and xmlpull with native scala xml
		import org.xmlpull.v1.XmlPullParser
		import org.xmlpull.v1.XmlPullParserException
		import org.xmlpull.v1.XmlPullParserFactory
		
		// TODO: replace all dom4j and xmlpull with native scala xml
		import org.dom4j.Element	
		import org.dom4j.io.XPPPacketReader
		
		import net.lag.naggati.IoHandlerActorAdapter
		import net.lag.naggati.MinaMessage
		import net.lag.naggati.ProtocolError
		
		import org.apache.mina.core.session.IoSession
		import org.apache.mina.core.buffer.IoBuffer
		import org.apache.mina.filter.codec.ProtocolCodecFilter
		import org.apache.mina.transport.socket.SocketAcceptor
		import org.apache.mina.transport.socket.nio.NioProcessor
		import org.apache.mina.transport.socket.nio.NioSocketAcceptor
		
		import scala.actors.Actor
		import scala.actors.Actor._
		
		import org.xmpp.codec._
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		import org.xmpp.protocol.component._
		
		import org.xmpp.util._
						
		object XmppComponent
		{
			val DEFAULT_TIMEOUT = 5 * 1000
			val xmlPullParserFactory = XmlPullParserFactory.newInstance
		}
		
		trait XmppComponent 
		{	
			private var _lastActive:Long = _
			
			// getters
			private var _subdomain:String = _
			final def subdomain:String = _subdomain
			
			private var _host:String = _
			final def host:String = _host
			
			private var _port:Int = _
			final def port:Int = _port
			
			private var _secret:String = _
			final def secret:String = _secret
			
			private var _timeout:Int = _
			final def timeout:Int = _timeout
			
			private var _handler:CommunicationHandler = _
					
			final def configure(subdomain:String, host:String, port:Int, secret:String, timeout:Int=0)
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
			
			final def start
			{
				start(this.subdomain, this.host, this.port, this.secret, this.timeout)
			}
			
			final def start(subdomain:String, host:String, port:Int, secret:String, timeout:Int=0)
			{
				configure(subdomain, host, port, secret, timeout)
				
				// TODO: look into this
				val domain = subdomain
				
				_handler = new CommunicationHandler
				_handler ! Connect(this.host, this.port, this.timeout, domain, this.secret, this.handleStanza)
				
				// start a thread to listen on server messages
				// 

				// keep alive
				//ScheduledJobsManager.registerJob("heartbeat", new HeartbeatJob(this.send, 60 * 1000))

				// cleanup job
				//ScheduledJobsManager.registerJob("cleanup", new CleanupJob(60 * 1000))
				//timeoutTask = new TimeoutTask
				//TaskEngine.getInstance.scheduleAtFixedRate(timeoutTask, 2000, 2000)
				
				//ScheduledJobsManager.startAll
					
				// TODO: add hook for implementations
			}
			
			final def shutdown
			{
				ScheduledJobsManager.stopAll
				_handler ! Disconnect
				// TODO: add hook for implementations
			}
			
			final def send(content:String) 
			{
				_handler ! Send(content)
				_lastActive = System.currentTimeMillis
			}
					
			protected def handleStanza(stanza:Stanza[_])
			
		}
		
		private case class Connect(host:String, port:Int, timeout:Int, domain:String, secret:String, stanzaHandler:(Stanza[_]) => Unit)
		private case class Disconnect
		private case class Send(content:String)
		private case class Exit
		
		private class CommunicationHandler extends Actor
		{
			private var _socket:Socket = _
			private var _reader:XPPPacketReader = _
			private var _writer:BufferedWriter = _
			private var _listener:ListenerThread = _
			//private var _connectionId:String = _
			//final def connectionId:String = _connectionId
			//final def connected:Boolean = null != _connectionId
						
			start
			
			def act
			{
				loop
				{
					react
					{					
						case Connect(host, port, timeout, domain, secret, stanzaHandler) => connect(host, port, timeout, domain, secret, stanzaHandler)
						case Disconnect => disconnect	
						case Send(content) => send(content)
						case Exit => exit
					}
				}
			}
			
			private def connect(host:String, port:Int, timeout:Int, domain:String, secret:String, stanzaHandler:(Stanza[_]) => Unit)
			{
				try 
				{
					_socket = new Socket
					_socket.connect(new InetSocketAddress(host, port), timeout)

					_reader = new XPPPacketReader
					_reader.setXPPFactory(XmppComponent.xmlPullParserFactory)
					_reader.getXPPParser.setInput(new InputStreamReader(_socket.getInputStream, "UTF-8"))
	
					_writer = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream, "UTF-8"))

					// start xmpp stream
					send("<stream:stream xmlns=\"jabber:component:accept\" xmlns:stream=\"http://etherx.jabber.org/streams\" to=\"" + domain + "\">")										
					val xpp:XmlPullParser = _reader.getXPPParser
					var eventType:Int = xpp.getEventType
					while (eventType != XmlPullParser.START_TAG) eventType = xpp.next	
					val connectionId = xpp.getAttributeValue("", "id")

					// handshake
					send(Handshake(connectionId, secret))					
					var doc:Element = _reader.parseDocument.getRootElement
					if ("error".equals(doc.getName)) 
					{
						// FIXME
						//val error = new StreamError(doc)
						throw new Exception("handshake error") 
					}
										
					// start a seperate thread to listen on server messages
					_listener = new ListenerThread(_reader, stanzaHandler)
					_listener.setDaemon(true)
					_listener.start									
				} 
				catch  
				{
					case e:Exception =>
					{
						disconnect
						throw new Exception("connection failed:" + e)
					} 
				}
			}
			
			private def disconnect
			{
				try 
				{
					if (null != _listener) _listener.shutdown
					
					if (null != _socket)
					{
						// end xmpp stream
						send("</stream:stream>")
						_socket.close
					}					
				}
				catch
				{
					case e:Exception => //TODO: do something intelligent here
				}
				finally
				{
					_socket = null
					_listener = null
				}
			}
		
			private def send(content:String) 
			{				
				if (null == _writer) return
				
				try 
				{
					_writer.write(content)
					_writer.flush
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
		}
		
		private class ListenerThread(reader:XPPPacketReader, stanzaHandler:(Stanza[_]) => Unit) extends Thread
		{
			private var _active = true
			
			override def run
			{
				// listen on port until noptified otherwise
				while (_active) 
				{
					val element = reader.parseDocument.getRootElement
					println ("stanza arrived")
					if (null != element) stanzaHandler(Stanza(element))					
				}				
			}
			
			def shutdown
			{
				_active = false
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