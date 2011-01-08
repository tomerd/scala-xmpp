package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Unavailable
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Unavailable =
			{					
				val xml = Presence.build(PresenceTypeEnumeration.Unavailable, id, to, from, None, None, None, None)
				return apply(xml)
			}

			def apply(xml:Node):Unavailable = new Unavailable(xml)
		}
		
		class Unavailable(xml:Node) extends Presence(xml, PresenceTypeEnumeration.Unavailable)
		{
		}
	}
}