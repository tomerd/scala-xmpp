package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Unsubscribe
		{
			val presenceType = PresenceTypeEnumeration.Unsubscribe
			val presenceTypeName = presenceType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(id:Option[String], to:JID, from:JID):Unsubscribe =
			{
				val xml = Presence.build(presenceType, id, to, from)
				return apply(xml)
			}
			
			def apply(xml:Node):Unsubscribe = new Unsubscribe(xml)
			
			def unapply(unsubscribe:Unsubscribe):Option[(Option[String], JID, JID, Option[Seq[Extension]])] = Some(unsubscribe.id, unsubscribe.to, unsubscribe.from, unsubscribe.extensions)
		}
		
		class Unsubscribe(xml:Node) extends Presence(xml, Unsubscribe.presenceType)
		{
		}
	}
}