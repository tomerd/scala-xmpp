package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

protected object AvailableBuilder
{
    def build(xml:Node):Available =
    {
        val show = (xml \ "show").text
        if (show.isEmpty) return Available(xml)

        Show.withName(show) match
        {
            case Show.Chat => Chat(xml)
            case Show.Away => Away(xml)
            case Show.XA => ExtendedAway(xml)
            case Show.DND => DoNotDisturb(xml)
            case _ => throw new Exception("unknown available presence stanza") // TODO, give a more detailed error message here
        }
    }
}

object Available
{
    val presenceType = PresenceTypeEnumeration.Available

    def apply(id:Option[String], to:Option[JID], from:Option[JID]):Available = apply(id, to, from, None, None, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Seq[Extension]):Available = apply(id, to, from, None, None, None, extensions)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], show:Show.Value):Available = apply(id, to, from, Some(show), None, None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], show:Show.Value, extensions:Seq[Extension]):Available = apply(id, to, from, Some(show), None, None, extensions)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], show:Show.Value, status:String):Available = apply(id, to, from, Some(show), Some(status), None, None)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], show:Show.Value, status:String, extensions:Seq[Extension]):Available = apply(id, to, from, Some(show), Some(status), None, extensions)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], show:Option[Show.Value], status:Option[String], priority:Option[Int], extensions:Option[Seq[Extension]]):Available = apply(build(id, to, from, show, status, priority, extensions))

    def apply(xml:Node, show:Option[Show.Value]=None):Available = new Available(xml, show)

    def build(id:Option[String], to:Option[JID], from:Option[JID]):Node = build(id, to, from, None, None, None, None)

    def build(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Node = build(id, to, from, None, None, None, extensions)

    def build(id:Option[String], to:Option[JID], from:Option[JID], show:Option[Show.Value], status:Option[String], priority:Option[Int], extensions:Option[Seq[Extension]]):Node =
    {
        val children = mutable.ListBuffer[Node]()
        if (!show.isEmpty) children += <show>{ show.get }</show>
        if (!status.isEmpty) children += <status>{ status.get }</status>
        if (!priority.isEmpty) children += <priority>{ priority.get }</priority>
        Presence.build(presenceType, id, to, from, children, extensions)
    }

    def unapply(available:Available):Option[(Option[String], Option[JID], Option[JID], Option[Show.Value], Option[String], Option[Int], Option[Seq[Extension]])] = Some(available.id, available.to, available.from, available.show, available.status, available.priority, available.extensions)

}

class Available(xml:Node, val show:Option[Show.Value]) extends Presence(xml, Available.presenceType)
{
    val status:Option[String] =
    {
        val status = (this.xml \ "status").text
        if (status.isEmpty) None else Some(status)
    }

    val priority:Option[Int] =
    {
        val priority = (this.xml \ "priority").text
        if (priority.isEmpty) None else Some(priority.toInt)
    }
}

object Show extends Enumeration
{
    type Reason = Value

    val Unknown = Value("unknown") // internal use
    val None = Value("") // not in spec
    val Chat = Value("chat")
    val Away = Value("away")
    val XA = Value("xa")
    val DND = Value("dnd")
}
