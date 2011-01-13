package org.xmpp.protocol
{
	package extensions.muc
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.presence._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object JoinRoom
		{
			val stanzaType = Available.stanzaTypeName
			val name = X.name
			val namespace = "http://jabber.org/protocol/muc"
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):JoinRoom =
			{
				val xml = Available.build(id, to, from, None, None, None, X(namespace))
				return apply(xml)
			}
				
			def apply(xml:Node):JoinRoom = JoinRoom(xml)			
		}
		
		class JoinRoom(xml:Node) extends Available(xml)
		{		
		}
	}	
}