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
			val stanzaType = PresenceTypeEnumeration.Subscribed
			val stanzaTypeName = stanzaType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Subscribed =
			{
				val xml = Presence.build(stanzaType, id, to, from, None, None, None, None)
				return apply(xml)
			}
			
			def apply(xml:Node):Subscribed = new Subscribed(xml)
		}
		
		class Subscribed(xml:Node) extends Presence(xml, Subscribed.stanzaType)
		{
		}
	}
}