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
			val kind = PresenceTypeEnumeration.Subscribed
			val kindName = kind.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Subscribed =
			{
				val xml = Presence.build(kind, id, to, from, None, None, None, None)
				return apply(xml)
			}
			
			def apply(xml:Node):Subscribed = new Subscribed(xml)
		}
		
		class Subscribed(xml:Node) extends Presence(xml, Subscribed.kind)
		{
		}
	}
}