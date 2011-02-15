package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Subscribed
		{
			val presenceType = PresenceTypeEnumeration.Subscribed
			val presenceTypeName = presenceType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(id:Option[String], to:JID, from:JID):Subscribed =
			{
				val xml = Presence.build(presenceType, id, to, from)
				return apply(xml)
			}
			
			def apply(xml:Node):Subscribed = new Subscribed(xml)
			
			def unapply(subscribed:Subscribed):Option[(Option[String], JID, JID, Option[Seq[Extension]])] = Some(subscribed.id, subscribed.to, subscribed.from, subscribed.extensions)
		}
		
		class Subscribed(xml:Node) extends Presence(xml, Subscribed.presenceType)
		{
		}
	}
}