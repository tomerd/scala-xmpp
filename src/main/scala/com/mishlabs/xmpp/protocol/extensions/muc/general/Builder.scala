package com.mishlabs.xmpp
package protocol
package extensions
package muc
package general

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object Builder extends ExtensionBuilder[X]
{
    val tag = X.tag
    val namespace = "http://jabber.org/protocol/muc"

    // FIXME: try to find a nicer way to do this, MUC standard is quite dirty
    def apply(xml:Node):X =
    {
        if (1 == (xml \ History.tag).length)
        {
            return History(xml)
        }
        else
        {
            return RoomPresence(xml)
        }
    }
}