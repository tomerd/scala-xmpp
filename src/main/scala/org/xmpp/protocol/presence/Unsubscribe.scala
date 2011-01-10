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
			val stanzaType = PresenceTypeEnumeration.Unsubscribe
			val stanzaTypeName = stanzaType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Unsubscribe =
			{
				val xml = Presence.build(stanzaType, id, to, from, None, None, None, None)
				return apply(xml)
			}
			
			def apply(xml:Node):Unsubscribe = new Unsubscribe(xml)
		}
		
		class Unsubscribe(xml:Node) extends Presence(xml, Unsubscribe.stanzaType)
		{
		}
	}
}