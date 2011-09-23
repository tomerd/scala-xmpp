package org.simbit.xmpp
{
	package client
	{
		import scala.collection._
		
		import org.simbit.xmpp.util._
		
		import org.simbit.xmpp.network._
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.presence._
		import org.simbit.xmpp.protocol.iq._
		import org.simbit.xmpp.protocol.message._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object XmppClient
		{
			val DEFAULT_PORT = 5280
		}
		
		class XmppClient extends NettyXmppClient with Logger
		{
			private var _jid:JID = null
			def jid = _jid
			
			private var _password:String = null
			
			final protected def identifier = _jid
			
			final protected def login(jid:JID, password:String, port:Int=XmppClient.DEFAULT_PORT)
			{
				_jid = jid
				_password = password
				
				super.connect(jid.domain, port)
			}
			
			final protected def logout
			{
				disconnect
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
							/*
							head.findAttribute("id") match
							{
								case Some(connectionId) => send(ComponentHandshake(connectionId, _secret))
								case None => throw new Exception("invaild stream head, connection id not found")
							}
							*/
						}
						case tail:StreamTail => debug(this.jid + " received stream tail") // TODO, do something more intelligent here?
						//case handshake:Handshake => debug(this.jid + " hanshake completed") 
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
					//case get @ Get(_, _, _, Some(info:disco.InfoRequest)) => handleDiscoInfo(get, info)
					//case get @ Get(_, _, _, Some(items:disco.ItemsRequest)) => handleDiscoItems(get, items)
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
		}
	}
}