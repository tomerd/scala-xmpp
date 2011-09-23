package org.simbit.xmpp
{
	package component
	{
		import java.io._
		import java.util._
		
		import java.io.IOException
		
		//import java.net.Socket
		//import java.net.InetSocketAddress
		
		import scala.collection._
		import scala.xml._
		
		/*
		import net.lag.naggati.IoHandlerActorAdapter
		import net.lag.naggati.MinaMessage
		import net.lag.naggati.ProtocolError
		
		import org.apache.mina.core.session.IoSession
		import org.apache.mina.filter.codec.ProtocolCodecFilter
		import org.apache.mina.filter.logging.LoggingFilter
		import org.apache.mina.transport.socket.nio.NioSocketConnector
		*/
		
		//import scala.actors.Actor
		//import scala.actors.Actor._
		import com.twitter.actors.Actor
		import com.twitter.actors.Actor._
		
		import org.simbit.xmpp.util._
		
		import org.simbit.xmpp.network._
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.presence._
		import org.simbit.xmpp.protocol.iq._
		import org.simbit.xmpp.protocol.message._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		trait XmppComponent extends NettyXmppClient with Logger
		{
			protected val identities:Seq[disco.Identity] = Nil		
			protected val features:Seq[disco.Feature] = Nil
			// TODO, not sure if i like this, perhaps need to leave it up to the component to decide if and when it wants to register the extension builders
			protected val extensionsBuilders:Seq[ExtensionBuilder[_ <: Extension]] = Nil
			
			private var _jid:JID = null
			def jid = _jid
			
			private var _subdomain:String = null
			def subdomain = _subdomain
			
			private var _secret:String = null
			
			final protected def identifier = _jid
			
			def start(subdomain:String, host:String, port:Int, secret:String /*, timeout:Int=0*/)
			{
				_jid = JID(null, subdomain + "." + host, null)
				_subdomain = subdomain
				_secret = secret
				
				// TODO, not sure if i like this, perhaps need to leave it up to the component to decide if and when it wants to register the extension builders
				extensionsBuilders.foreach( builder => ExtensionsManager.registerBuilder(builder) )
				
				connect(host, port /*, timeout*/)
				
				// TODO: add hook for implementations
			}
			
			def shutdown
			{
				try
				{
					send(StreamTail())
				}
				catch
				{
					// TODO: do something intelligent here
					case e:Exception => error(this.jid + " shutdown error " + e) 
				}
				finally
				{
					disconnect
				}
			}
			
			final override protected def handleConnected
			{
				super.handleConnected
				
				debug(this.jid + " sending stream head")				
				send(StreamHead("jabber:component:accept", immutable.List("to" -> this.jid)))
			}
			
			final override protected def handle(packet:Packet)
			{
				super.handle(packet)
				
				try
				{
					packet match
					{
						case head:StreamHead =>
						{
							debug(this.jid + " received stream head")
							
							head.findAttribute("id") match
							{
								case Some(connectionId) => send(ComponentHandshake(connectionId, _secret))
								case None => throw new Exception("invaild stream head, connection id not found")
							}
						}
						case tail:StreamTail => debug(this.jid + " received stream tail") // TODO, do something more intelligent here?
						case handshake:Handshake => info(this.jid + " hanshake completed, xmpp route established") 
						case error:StreamError => throw new Exception("stream error: " + error.condition + ", " + error.description.getOrElse(""))
						case stanza:Stanza => handleStanza(stanza)
						case stanzas:Seq[Stanza] => stanzas.foreach( stanza => handleStanza(stanza) )
						case _ => throw new Exception("unknown xmpp packet")
					}
				}
				catch
				{
					case e:Exception => 
					{
						error(this.jid + " error handling packet: " + e)
						disconnect
					}
				}
			}
			
			private def handleStanza(stanza:Stanza) 
			{
				stanza match
			 	{
					case presence:Presence => handlePresence(presence)
					//TODO, find a better way to do the disco match, ideally we would match on a well defined disco instead of matching on the extension	
					case get @ Get(_, _, _, Some(info:disco.InfoRequest)) => handleDiscoInfo(get, info)
					case get @ Get(_, _, _, Some(items:disco.ItemsRequest)) => handleDiscoItems(get, items)
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
			
			private def handleDiscoInfo(request:Get, infoRequest:disco.InfoRequest)
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
			protected def getChildDiscoInfo(jid:JID, request:disco.InfoRequest):Option[disco.InfoResult] = None
			
			private def handleDiscoItems(request:Get, itemsRequest:disco.ItemsRequest)
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
			protected def getDiscoItems(request:disco.ItemsRequest):Option[disco.ItemsResult] = None
			
			// to be implemented by sub classes as required	
			protected def getChildDiscoItems(jid:JID, request:disco.ItemsRequest):Option[disco.ItemsResult] = None
			
		}
		
	}
}
