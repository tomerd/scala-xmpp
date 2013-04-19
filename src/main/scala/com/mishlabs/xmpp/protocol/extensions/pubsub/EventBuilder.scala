package com.mishlabs.xmpp
package protocol
package extensions
package pubsub

import scala.collection._
import scala.xml._

import Protocol._

object EventBuilder extends ExtensionBuilder[Event]
{
    val tag = Event.tag
    val namespace = Builder.namespace + "#event"

    // FIXME: implement this
    def apply(xml:Node):Event = Event(xml)

}
