package com.mishlabs.xmpp
package protocol
package extensions
package disco

import scala.xml._

import Protocol._

object NodeItem
{
    def apply(jid:JID, node:String, name:Option[String]=None):NodeItem = apply(build(jid, node, name))

    def apply(xml:Node):NodeItem = new NodeItem(xml)

    def build(jid:JID, node:String, name:Option[String]):Node =
    {
        var metadata:MetaData = new UnprefixedAttribute("jid", Text(jid.get), Null)
        metadata = metadata.append(new UnprefixedAttribute("node", Text(node.get), Null))
        if (!name.isEmpty) metadata = metadata.append(new UnprefixedAttribute("name", Text(name.get), Null))
        return Item.build(metadata)
    }
}

class NodeItem(xml:Node) extends Item(xml)
{
    val jid:JID = (this.xml \ "@jid").text

    val node:String = (this.xml \ "@node").text

    val name:Option[String] = (this.xml \ "@name").text
}