package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object PresenceFactory extends StanzaFactory[Presence]
		{
			def create(xml:Node):Presence = 
			{
				(xml \ "@type").text match
				{
					// FIXME, use enum values instead of hard coded string here (getting compilation error. even with implicict cast)
					case "" => Available(xml) // PresenceTypeEnumeration.Available
					case "unavailable" => Unavailable(xml) // PresenceTypeEnumeration.Unavailable					
					case "subscribe" => Subscribe(xml) // PresenceTypeEnumeration.Subscribe
					case "subscribed" => Subscribed(xml) // PresenceTypeEnumeration.Subscribed					
					case "unsubscribe" => Unsubscribe(xml) // PresenceTypeEnumeration.Unsubscribe
					case "unsubscribed" => Unsubscribed(xml) // PresenceTypeEnumeration.Unsubscribed
					case "probe" => Probe(xml) // PresenceTypeEnumeration.Probe
					case "error" => Error(xml) // PresenceTypeEnumeration.Error
					case _ => throw new Exception("unknown presence stanza") // TODO, give a more detailed error message here
				}
			}
		}
		
	}
}