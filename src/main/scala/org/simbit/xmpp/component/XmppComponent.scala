package org.simbit.xmpp
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
		
		import org.simbit.xmpp.util._
		
		import org.simbit.xmpp.codec._
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.presence._		
		import org.simbit.xmpp.protocol.iq._
		import org.simbit.xmpp.protocol.message._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object XmppComponent
		{
			// TODO: these should come from a configuration file
			val DEFAULT_TIMEOUT = 5 * 1000
			val KEEPALIVE_INTERVAL = 60 * 1000
			val CLEANUP_INTERVAL = 5 * 60 * 1000
			val MAX_RECONNECT_ATTEMPTS = 5
		}
		
		trait XmppComponent extends Logger
		{
			protected val identities:Seq[disco.Identity] = Nil		
			protected val features:Seq[disco.Feature] = Nil
			// TODO, not sure if i like this, perhaps need to leave it up to the component to decide if and when it wants to register the extension builders
			protected val extensionsBuilders:Seq[ExtensionBuilder[_ <: Extension]] = Nil
			
			private var _jid:JID = null
			def jid = _jid
			
			private var _subdomain:String = null
			def subdomain = _subdomain
			
			private var _host:String = null
			def host = _host
			
			private var _port:Int = 0
			def port = _port
			
			private var _secret:String = null
			def secret = _secret
			
			private var _timeout:Int = XmppComponent.DEFAULT_TIMEOUT
			def timeout = _timeout
						
			private var _connector:NioSocketConnector = null
			private var _session:IoSession = null
			
			private var _shuttingdown = false
			private var _reconnectAttampts = 0
			
			private var _lastActive:Long = 0
			
			def configure(subdomain:String, host:String, port:Int, secret:String, timeout:Int=0)
			{
				_jid = JID(null, subdomain + "." + host, null)
				_subdomain = subdomain
				_host = host
				_port = port
				_secret = secret
				_timeout = if (0 == timeout) timeout else XmppComponent.DEFAULT_TIMEOUT
								
				require(null != this.subdomain)
				require(null != this.host)
				require(0 != this.port)
				require(null != this.secret)
				
				// TODO, not sure if i like this, perhaps need to leave it up to the component to decide if and when it wants to register the extension builders
				extensionsBuilders.foreach( builder => ExtensionsManager.registerBuilder(builder) )
			}
			
			def start
			{
				start(this.subdomain, this.host, this.port, this.secret, this.timeout)
			}
			
			def start(subdomain:String, host:String, port:Int, secret:String, timeout:Int=0)
			{
				configure(subdomain, host, port, secret, timeout)
				connect
				startJobs
				
				// TODO: add hook for implementations
			}
			
			private def connect
			{
				// start a mina thread pool listening on server messages
				_connector = new NioSocketConnector()
				_connector.setConnectTimeout(timeout)
				_connector.getFilterChain.addLast("codec", new ProtocolCodecFilter(Codec.encoder, Codec.decoder))
				_connector.getFilterChain.addLast("logger", new LoggingFilter())
				_connector.setHandler(new IoHandlerActorAdapter(session => new XmppHandler(session, this.jid, this.secret, this.handleConnect, this.handleDisconnect, this.handleStanza)))
				
				val future = _connector.connect(new InetSocketAddress(host, port))
				future.awaitUninterruptibly
				_session = future.getSession
			}
			
			private def startJobs
			{
				ScheduledJobsManager.registerJob("keeplalive_" + this.subdomain, this.keepalive, XmppComponent.KEEPALIVE_INTERVAL)
				ScheduledJobsManager.registerJob("cleanup_" + this.subdomain, this.cleanup, XmppComponent.CLEANUP_INTERVAL)				
				ScheduledJobsManager.startAll
			}
			
			def shutdown
			{
				try
				{					
					_shuttingdown = true
					send(StreamTail())
					if (null != _session) _session.getCloseFuture.awaitUninterruptibly
					if (null != _connector) _connector.dispose
					
					ScheduledJobsManager.stopAll
				}
				catch
				{
					// TODO: do something intelligent here
					case e:Exception => error(this.jid + " disconnect error " + e) 
				}
				finally
				{
					_session = null
					_connector = null
				}
			}
			
			def send(content:String) 
			{
				if (null == _session) return
				
				try 
				{
					touch
					_session.write(content)
				}
				catch
				{
					// TODO: do something intelligent here
					case e:Exception => error(this.jid + " failed sending message to xmpp server");
				}
			}

			private def handleConnect()
			{
				_reconnectAttampts = 0
				info(this.jid + " is connected to xmpp server");
			}
						
			private def handleDisconnect()
			{
				if (_shuttingdown) return
				if (_reconnectAttampts < XmppComponent.MAX_RECONNECT_ATTEMPTS)
				{
					error(this.jid + " is was disconnected, attmepting to reconnect (attempt #" + (_reconnectAttampts+1) + ")")
					_reconnectAttampts = _reconnectAttampts+1
					connect
					
				}
				else
				{
					error(this.jid + " is was disconnected " + XmppComponent.MAX_RECONNECT_ATTEMPTS + " times in a row, shutting down")
				}
			}
						
			protected def handleStanza(stanza:Stanza) 
			{
				touch
				//TODO, find a better way to do the disco match, ideally we would match on a well defined disco instead of matching on the extension
				stanza match
			 	{
					case presence:Presence => handlePresence(presence)	
					case get @ Get(_, _, _, Some(info:disco.Info)) => handleDiscoInfo(get, info)
					case get @ Get(_, _, _, Some(items:disco.Items)) => handleDiscoItems(get, items)
					case iq:IQ => handleIQ(iq)
					case message:Message => handleMessage(message)
				}				
			}
			
			// to be implemented by subclasses as required
			protected def handlePresence(presence:Presence) 
			{
			}

			// to be implemented by subclasses as required
		    protected def handleIQ(iq:IQ)
		    {
		    }

		    // to be implemented by subclasses as required
			protected def handleMessage(message:Message) 
			{
			}

			private def handleDiscoInfo(request:Get, infoRequest:disco.Info)
			{				
				if (request.to == this.jid)
				{
					send(request.result(infoRequest.result(this.identities, this.features)))
				}
				else getChildDiscoInfo(request.to, infoRequest) match
				{
					case Some(info) => send(request.result(info))
					case _ => // do nothing					
				}
				/*
				request.to match
				{
					case Some(jid) if jid == this.jid => 
					{
						send(request.result(infoRequest.result(this.identities, this.features)))
					}
					case _ => getChildDiscoInfo(request.to.get, infoRequest) match
					{
						case Some(info) => send(request.result(info))
						case _ => // do nothing					
					}
				}
				*/
			}
			
			// to be implemented by sub classes as required
			protected def getChildDiscoInfo(jid:JID, request:disco.Info):Option[disco.InfoResult] = None
			
			private def handleDiscoItems(request:Get, itemsRequest:disco.Items)
			{	
				if (request.to == this.jid) getDiscoItems(itemsRequest) match 
				{
					case Some(items) => send(request.result(items))
					case _ => // do nothing						
				}
				else getChildDiscoItems(request.to, itemsRequest) match
				{
					case Some(items) => send(request.result(items))
					case _ => // do nothing
				}
				/*
				request.to match
				{
					case Some(jid) if jid == this.jid => getDiscoItems(itemsRequest) match 
					{
						case Some(items) => send(request.result(items))
						case _ => // do nothing						
					}
					case _ => getChildDiscoItems(request.to.get, itemsRequest) match
					{
						case Some(items) => send(request.result(items))
						case _ => // do nothing
					}
				}
				*/
			}
			
			// to be implemented by sub classes as required	
			protected def getDiscoItems(request:disco.Items):Option[disco.ItemsResult] = None
			
			// to be implemented by sub classes as required	
			protected def getChildDiscoItems(jid:JID, request:disco.Items):Option[disco.ItemsResult] = None
			
			// to be implemented by sub classes as required
			protected def cleanup()
			{
			}
			
			private def touch
			{
				_lastActive = new Date().getTime
			}
			
			private def keepalive()
			{
				// dont over do it
				if (new Date().getTime - _lastActive < XmppComponent.KEEPALIVE_INTERVAL/2) return
				send(" ")
			}
		}
		
		private object XmppHandler
		{
			// TODO: this should come from a configuration file
			val MAX_ERRORS = 10
			val READ_BUFFER_SIZE = 2048
		}
				
		private class XmppHandler(val session:IoSession, val jid:JID, val secret:String, val connectHandler:() => Unit, val disconnectHandler:() => Unit, val stanzaHandler:(Stanza) => Unit) extends Actor with Logger
		{
			private var _errors = 0
			
			session.getConfig.setReadBufferSize(XmppHandler.READ_BUFFER_SIZE)
			IoHandlerActorAdapter.filter(session) -= classOf[MinaMessage.MessageSent]
			IoHandlerActorAdapter.filter(session) -= classOf[MinaMessage.SessionIdle]
			
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
							val head = StreamHead("jabber:component:accept", immutable.List("to" -> this.jid))
							send(head)
						}
						case MinaMessage.SessionClosed => 
						{
							disconnectHandler()
							exit
						}
						case MinaMessage.MessageReceived(message) => 
						{
							_errors = 0
							handle(message)
						}						
						case MinaMessage.ExceptionCaught(cause) => 
						{
							if (_errors < XmppHandler.MAX_ERRORS)
							{
								_errors = _errors+1
								error(this.jid + " suffered internal error: " + cause.getCause)								
							}
							else
							{
								error(this.jid + " suffered " + XmppHandler.MAX_ERRORS + " internal error in a row, disconnecting")
								session.close(false)
							}
						}
					}
				}
			}
			
  			private def handle(message:AnyRef) = 
			{
  				try
  				{
					message match
					{
	  					case head:StreamHead =>
						{
							head.findAttribute("id") match
							{
								case Some(connectionId) => send(ComponentHandshake(connectionId, secret))
								case None => throw new Exception("invaild stream head, connection id not found")
							}
						}					
						case tail:StreamTail => debug(this.jid + " received stream tail") // TODO, do something more intelligent here?
						case handshake:Handshake => connectHandler()
						case error:StreamError => throw new Exception("stream error: " + error.condition + ", " + error.description.getOrElse(""))			
						case stanza:Stanza => handleStanza(stanza)
						case stanzas:Seq[Stanza] => stanzas.foreach( stanza => handleStanza(stanza) )
						case _ => throw new Exception("unknown message")
					}
  				}
  				catch
  				{
  					case e:Exception => 
  					{
  						error(this.jid + " error: " + e)
  						session.close(false)
  					}
  				}
			}
  			
  			private def handleStanza(stanza:Stanza)
  			{
  				debug(stanza.to + " recieved stanza from: " + stanza.from)
				
				try
				{
					stanzaHandler(stanza)
				}
				catch
				{
					// TODO, do something more intelligent here?
					case e:Exception => error(this.jid + " failed handling stanza " + e + "\n" + stanza)
				}
  			}
			
			private def send(content:String) = 
			{
				session.write(content)
			}
		}
				
	}
}
