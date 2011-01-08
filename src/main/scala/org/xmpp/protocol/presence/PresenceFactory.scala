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
			def create(xml:Node):Option[Presence] = 
			{
				xml match
				{
					// FIXME, handle other cases here
					case root @ <presence>{ extensions @ _* }</presence> if (root \ "@type").text == PresenceTypeEnumeration.Available.toString => Some(Available(root))
					case root @ <presence>{ extensions @ _* }</presence> if (root \ "@type").text == PresenceTypeEnumeration.Unavailable.toString => Some(Unavailable(root))
					case root @ <presence>{ extensions @ _* }</presence> if (root \ "@type").text == PresenceTypeEnumeration.Subscribe.toString => Some(Subscribe(root))
					case root @ <presence>{ extensions @ _* }</presence> if (root \ "@type").text == PresenceTypeEnumeration.Subscribed.toString => Some(Subscribed(root))
					case root @ <presence>{ extensions @ _* }</presence> if (root \ "@type").text == PresenceTypeEnumeration.Unsubscribe.toString => Some(Unsubscribe(root))
					case root @ <presence>{ extensions @ _* }</presence> if (root \ "@type").text == PresenceTypeEnumeration.Unsubscribed.toString => Some(Unsubscribed(root))
					case root @ <presence>{ extensions @ _* }</presence> if (root \ "@type").text == PresenceTypeEnumeration.Probe.toString => Some(Probe(root))
					case root @ <presence>{ extensions @ _* }</presence> if (root \ "@type").text == PresenceTypeEnumeration.Error.toString => Some(Error(root))
					case _ => None			
				}
			}
		}
		
	}
}