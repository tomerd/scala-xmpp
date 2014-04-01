package com.mishlabs.xmpp
package protocol
package message

import scala.collection._
import scala.xml._

import extensions._
import Protocol._

private[xmpp] object Builder
{
    def apply(xml:Node):Message =
    {
        require("message" == xml.label)

        MessageTypeEnumeration.fromString((xml \ "@type").text) match
        {
            case MessageTypeEnumeration.Normal => Normal(xml)
            case MessageTypeEnumeration.Chat => Chat(xml)
            case MessageTypeEnumeration.GroupChat => GroupChat(xml)
            case MessageTypeEnumeration.Headline => Headline(xml)
            case MessageTypeEnumeration.Extended => Extended(xml)
            case MessageTypeEnumeration.Error => Error(xml)
            case _ => throw new Exception("unknown message stanza") // TODO, give a more detailed error message here
        }
    }
}
