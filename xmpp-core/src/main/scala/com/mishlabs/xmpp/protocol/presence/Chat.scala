package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

object Chat
{
    def apply(id:Option[String], to:Option[JID], from:Option[JID], status:Option[String], priority:Option[Int], extensions:Option[Seq[Extension]]):Chat =
    {
        val xml = Available.build(id, to, from, Some(Show.Chat), status, priority, extensions)
        return apply(xml)
    }

    def apply(xml:Node):Chat = new Chat(xml)
}

class Chat(xml:Node) extends Available(xml/*, Some(Show.Chat)*/)
{
}
