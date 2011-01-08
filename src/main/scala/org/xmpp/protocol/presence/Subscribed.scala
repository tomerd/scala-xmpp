package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Subscribed
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Subscribed =
			{					
				val xml = Presence.build(PresenceTypeEnumeration.Subscribed, id, to, from, None, None, None, None)
				return apply(xml)
			}
			
			def apply(xml:Node):Subscribed = new Subscribed(xml)
		}
		
		class Subscribed(xml:Node) extends Presence(xml, PresenceTypeEnumeration.Subscribed)
		{
		}
	}
}