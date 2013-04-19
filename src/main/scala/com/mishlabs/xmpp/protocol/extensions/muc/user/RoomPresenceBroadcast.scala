package com.mishlabs.xmpp
package protocol
package extensions
package muc
package user

import scala.collection._
import scala.xml._

import Protocol._

object RoomPresenceBroadcast
{
    def apply(affiliation:Affiliation.Value, role:Role.Value):RoomPresenceBroadcast = apply(affiliation, role, None, None, None)

    def apply(affiliation:Affiliation.Value, role:Role.Value, actor:Option[JID], reason:Option[String]):RoomPresenceBroadcast = apply(affiliation, role, actor, reason, None)

    def apply(affiliation:Affiliation.Value, role:Role.Value, status:Int):RoomPresenceBroadcast = apply(affiliation, role, None, None, List(status))

    def apply(affiliation:Affiliation.Value, role:Role.Value, statuses:Seq[Int]):RoomPresenceBroadcast = apply(affiliation, role, None, None, statuses)

    def apply(affiliation:Affiliation.Value, role:Role.Value, actor:Option[JID], reason:Option[String], statuses:Option[Seq[Int]]):RoomPresenceBroadcast =
    {
        val itemChildren = mutable.ListBuffer[Node]()
        if (!actor.isEmpty) itemChildren += <actor>{ actor.get }</actor>
        if (!reason.isEmpty) itemChildren += <reason>{ reason.get }</reason>

        var itemMetadata:MetaData = new UnprefixedAttribute("affiliation", Text(affiliation.toString), Null)
        itemMetadata = itemMetadata.append(new UnprefixedAttribute("role", Text(role.toString), Null))

        val children = mutable.ListBuffer[Node]()
        children += Elem(null, "item", itemMetadata, TopScope, itemChildren:_*)
        if (!statuses.isEmpty) statuses.get.foreach( status => children += <status code={ status.toString } /> )

        return apply(Builder.build(children))
    }

    def apply(xml:Node):RoomPresenceBroadcast = new RoomPresenceBroadcast(xml)
}

class RoomPresenceBroadcast(xml:Node) extends X(xml)
{
    private val itemNode = (xml \ "item")(0)

    val affiliation:Affiliation.Value = Affiliation.withName((this.itemNode \ "@affiliation").text)

    val role:Role.Value = Role.withName((this.itemNode \ "@role").text)

    val statuses:Option[Seq[Int]] =
    {
        val nodes = (xml \ "status")
        nodes.length match
        {
            case 0 => None
            case _ => Some(nodes.map ( node => node.attributes("code").text.toInt ))
        }
    }
}