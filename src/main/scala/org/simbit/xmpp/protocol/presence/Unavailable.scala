package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Unavailable
		{
			val presenceType = PresenceTypeEnumeration.Unavailable
			val presenceTypeName = presenceType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Unavailable =
			{
				val xml = Presence.build(presenceType, id, to, from)
				return apply(xml)
			}

			def apply(xml:Node):Unavailable = new Unavailable(xml)
			
			def unapply(unavailable:Unavailable):Option[(Option[String], Option[JID], Option[JID], Option[Seq[Extension]])] = Some(unavailable.id, unavailable.to, unavailable.from, unavailable.extensions)
		}
		
		class Unavailable(xml:Node) extends Presence(xml, Unavailable.presenceType)
		{
		}
	}
}