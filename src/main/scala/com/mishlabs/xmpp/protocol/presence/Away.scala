package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

object Away
{
    def apply(id:Option[String], to:Option[JID], from:Option[JID], status:Option[String], priority:Option[Int], extensions:Option[Seq[Extension]]):Away =
    {
        val xml = Available.build(id, to, from, Some(Show.Away), status, priority, extensions)
        return apply(xml)
    }

    def apply(xml:Node):Away = new Away(xml)
}

class Away(xml:Node) extends Available(xml, Some(Show.Away))
{
}