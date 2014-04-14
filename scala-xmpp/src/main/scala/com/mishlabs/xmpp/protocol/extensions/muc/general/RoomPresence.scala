package com.mishlabs.xmpp
package protocol
package extensions
package muc
package general

import scala.collection._
import scala.xml._

import Protocol._

object RoomPresence
{
    def apply():RoomPresence = apply(Builder.build())

    def apply(xml:Node):RoomPresence = new RoomPresence(xml)
}

class RoomPresence(xml:Node) extends X(xml)
{
}