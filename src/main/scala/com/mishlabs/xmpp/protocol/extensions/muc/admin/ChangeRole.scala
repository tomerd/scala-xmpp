package com.mishlabs.xmpp
package protocol
package extensions
package muc
package admin

import scala.collection._
import scala.xml._

import Protocol._

object ChangeRole
{
    def apply(nick:String, role:Role.Value, reason:Option[String]):ChangeRole =
    {
        val itemChildren = mutable.ListBuffer[Node]()
        if (!reason.isEmpty) itemChildren += <reason>{ reason }</reason>

        var itemMetadata:MetaData = new UnprefixedAttribute("nick", Text(nick), Null)
        itemMetadata = itemMetadata.append(new UnprefixedAttribute("role", Text(role.toString), Null))

        val children = mutable.ListBuffer[Node]()
        children += Elem(null, "item", itemMetadata, TopScope, itemChildren:_*)

        return apply(Builder.build(children))
    }

    def apply(xml:Node):ChangeRole = ChangeRole(xml)
}

class ChangeRole(xml:Node) extends X(xml)
{
    private val itemNode = (xml \ "item")(0)

    val nick:String = (this.itemNode \ "@nick").text

    val role:Role.Value = Role.withName((this.itemNode \ "@role").text)

    val reason:Option[String] =
    {
        val nodes = (this.itemNode \ "reason")
        nodes.length match
        {
            case 0 => None
            case _ => Some(nodes(0).text)
        }
    }
}