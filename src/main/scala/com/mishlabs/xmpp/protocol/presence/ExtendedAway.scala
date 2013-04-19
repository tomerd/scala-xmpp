package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

object ExtendedAway
{
    def apply(id:Option[String], to:Option[JID], from:Option[JID], status:Option[String], priority:Option[Int], extensions:Option[Seq[Extension]]):ExtendedAway =
    {
        val xml = Available.build(id, to, from, Some(Show.XA), status, priority, extensions)
        return apply(xml)
    }

    def apply(xml:Node):ExtendedAway = new ExtendedAway(xml)
}

class ExtendedAway(xml:Node) extends Available(xml, Some(Show.XA))
{
}