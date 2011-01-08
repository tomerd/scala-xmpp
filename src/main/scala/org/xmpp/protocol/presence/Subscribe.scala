package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Subscribe
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Subscribe =
			{					
				val xml = Presence.build(PresenceTypeEnumeration.Subscribe, id, to, from, None, None, None, None)
				return apply(xml)
			}
			
			def apply(xml:Node):Subscribe = new Subscribe(xml)
		}
		
		class Subscribe(xml:Node) extends Presence(xml, PresenceTypeEnumeration.Subscribe)
		{
		}
	}
}