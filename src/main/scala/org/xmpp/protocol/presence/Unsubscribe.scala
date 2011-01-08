package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Unsubscribe
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Unsubscribe =
			{					
				val xml = Presence.build(PresenceTypeEnumeration.Unsubscribe, id, to, from, None, None, None, None)
				return apply(xml)
			}
			
			def apply(xml:Node):Unsubscribe = new Unsubscribe(xml)
		}
		
		class Unsubscribe(xml:Node) extends Presence(xml, PresenceTypeEnumeration.Unsubscribe)
		{
		}
	}
}