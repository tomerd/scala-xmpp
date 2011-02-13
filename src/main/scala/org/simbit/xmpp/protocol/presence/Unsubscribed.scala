package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Unsubscribed
		{
			val presenceType = PresenceTypeEnumeration.Unsubscribed
			val presenceTypeName = presenceType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Unsubscribed =
			{
				val xml = Presence.build(presenceType, id, to, from)
				return apply(xml)
			}

			def apply(xml:Node):Unsubscribed = new Unsubscribed(xml)
			
			def unapply(unsubscribed:Unsubscribed):Option[(Option[String], Option[JID], Option[JID], Option[Seq[Extension]])] = Some(unsubscribed.id, unsubscribed.to, unsubscribed.from, unsubscribed.extensions)
		}
		
		class Unsubscribed(xml:Node) extends Presence(xml, Unsubscribe.presenceType)
		{
		}
	}
}