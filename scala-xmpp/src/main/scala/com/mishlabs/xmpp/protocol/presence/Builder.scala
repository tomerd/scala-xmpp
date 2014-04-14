package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object Builder
{
    def apply(xml:Node):Presence =
    {
        require("presence" == xml.label)

        PresenceTypeEnumeration.fromString((xml \ "@type").text) match
        {
            case PresenceTypeEnumeration.Available => AvailableBuilder.build(xml) // PresenceTypeEnumeration.Available
            case PresenceTypeEnumeration.Unavailable => Unavailable(xml) // PresenceTypeEnumeration.Unavailable
            case PresenceTypeEnumeration.Subscribe => Subscribe(xml) // PresenceTypeEnumeration.Subscribe
            case PresenceTypeEnumeration.Subscribed => Subscribed(xml) // PresenceTypeEnumeration.Subscribed
            case PresenceTypeEnumeration.Unsubscribe => Unsubscribe(xml) // PresenceTypeEnumeration.Unsubscribe
            case PresenceTypeEnumeration.Unsubscribed => Unsubscribed(xml) // PresenceTypeEnumeration.Unsubscribed
            case PresenceTypeEnumeration.Probe => Probe(xml) // PresenceTypeEnumeration.Probe
            case PresenceTypeEnumeration.Error => Error(xml) // PresenceTypeEnumeration.Error
            case _ => throw new Exception("unknown presence stanza") // TODO, give a more detailed error message here
        }
    }
}