package com.mishlabs.xmpp
package protocol
package extensions
package pubsub

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object Builder extends ExtensionBuilder[Pubsub]
{
    val tag = Pubsub.tag
    val namespace = "http://jabber.org/protocol/pubsub"

    // FIXME: implement this
    def apply(xml:Node):Pubsub = Pubsub(xml)

}