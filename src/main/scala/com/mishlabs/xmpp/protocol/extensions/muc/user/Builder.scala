package com.mishlabs.xmpp
package protocol
package extensions
package muc
package user

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object Builder extends ExtensionBuilder[X]
{
    val tag = X.tag
    val namespace = com.mishlabs.xmpp.protocol.extensions.muc.general.Builder.namespace + "#user"

    // FIXME: try to find a nicer way to do this, MUC standard is quite dirty
    def apply(xml:Node):X =
    {
        if (1 == (xml \ Invite.tag).length)
        {
            return Invite(xml)
        }
        else if (1 == (xml \ Decline.tag).length)
        {
            return Decline(xml)
        }
        else if (1 == (xml \ Item.tag).length)
        {
            return RoomPresenceBroadcast(xml)
        }
        else
        {
            return X(xml)
        }
    }
}