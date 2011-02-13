package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Subscribe
		{
			val presenceType = PresenceTypeEnumeration.Subscribe
			val presenceTypeName = presenceType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Subscribe =
			{
				val xml = Presence.build(presenceType, id, to, from)
				return apply(xml)
			}
			
			def apply(xml:Node):Subscribe = new Subscribe(xml)
			
			def unapply(subscribe:Subscribe):Option[(Option[String], Option[JID], Option[JID], Option[Seq[Extension]])] = Some(subscribe.id, subscribe.to, subscribe.from, subscribe.extensions)
		}
		
		class Subscribe(xml:Node) extends Presence(xml, Subscribe.presenceType)
		{
		}
	}
}