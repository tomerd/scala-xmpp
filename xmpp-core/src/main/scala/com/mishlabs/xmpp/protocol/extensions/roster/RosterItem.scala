package com.mishlabs.xmpp
package protocol
package extensions
package roster

import scala.collection._
import scala.xml._

import Protocol._

object RosterItem
{
    def apply(jid:JID):RosterItem = apply(jid, None, None, None, None)

    def apply(jid:JID, name:Option[String]):RosterItem = apply(jid, name, None, None, None)

    def apply(jid:JID, name:Option[String], subscription:Option[ItemSubscription.Value], ask:Option[ItemAsk.Value], groups:Option[Seq[String]]):RosterItem = apply(build(jid, name, subscription, ask, groups))

    def apply(xml:Node):RosterItem = new RosterItem(xml)

    def build(jid:JID, name:Option[String], subscrption:Option[ItemSubscription.Value], ask:Option[ItemAsk.Value], groups:Option[Seq[String]]):Node =
    {
        val children = mutable.ListBuffer[Node]()
        if (!groups.isEmpty) groups.get.foreach( group => children += <groups>{ group }</groups> )
        var metadata:MetaData = new UnprefixedAttribute("jid", Text(jid.get), Null)
        if (!name.isEmpty) metadata = metadata.append(new UnprefixedAttribute("name", Text(name.get), Null))
        if (!subscrption.isEmpty) metadata = metadata.append(new UnprefixedAttribute("subscrption", Text(subscrption.get.toString), Null))
        if (!ask.isEmpty) metadata = metadata.append(new UnprefixedAttribute("ask", Text(ask.get.toString), Null))
        return Item.build(metadata, children)
    }
}

class RosterItem(xml:Node) extends Item(xml)
{
    val jid:JID = (this.xml \ "@jid").text

    val name:Option[String] = (this.xml \ "@name").text

    val subscrption:Option[ItemSubscription.Value] =
    {
        val subscrption = (this.xml \ "@subscrption").text
        if (subscrption.isEmpty) None else Some(ItemSubscription.withName(subscrption))
    }

    val ask:Option[ItemAsk.Value] =
    {
        val ask = (this.xml \ "@ask").text
        if (ask.isEmpty) None else Some(ItemAsk.withName(ask))
    }

    val groups:Option[Seq[String]] =
    {
        val groupNodes = (this.xml \ "group")
        if (0 == groupNodes.length) None else Some( groupNodes.map( node => node.label ) )
    }
}

object ItemSubscription extends Enumeration
{
    type value = Value

    val Unknown = Value("unknown") // internal use
    val None = Value("none")
    val To = Value("to")
    val From = Value("from")
    val Both = Value("both")
    val Remove = Value("remove")
}

object ItemAsk extends Enumeration
{
    type value = Value

    val Unknown = Value("unknown") // internal use
    val Subscribe = Value("subscribe")
    val Unsubscribe = Value("unsubscribe")
}