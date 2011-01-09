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
					// FIXME, use the enum values (attribute kind) instead of kindName, getting compilation error even with implicict cast
					case Available.kindName => Available(xml) // PresenceTypeEnumeration.Available
					case Unavailable.kindName => Unavailable(xml) // PresenceTypeEnumeration.Unavailable					
					case Subscribe.kindName => Subscribe(xml) // PresenceTypeEnumeration.Subscribe
					case Subscribed.kindName => Subscribed(xml) // PresenceTypeEnumeration.Subscribed					
					case Unsubscribe.kindName => Unsubscribe(xml) // PresenceTypeEnumeration.Unsubscribe
					case Unsubscribed.kindName => Unsubscribed(xml) // PresenceTypeEnumeration.Unsubscribed
					case Probe.kindName => Probe(xml) // PresenceTypeEnumeration.Probe
					case Error.kindName => Error(xml) // PresenceTypeEnumeration.Error
					case _ => throw new Exception("unknown presence stanza") // TODO, give a more detailed error message here
				}
			}
		}
		
	}
}