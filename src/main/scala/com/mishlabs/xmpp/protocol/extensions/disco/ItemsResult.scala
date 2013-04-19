package com.mishlabs.xmpp
package protocol
package extensions
package disco

import scala.collection._
import scala.xml._

import Protocol._

object ItemsResult
{
    def apply(items:Seq[Item]):ItemsResult = apply(None, items)

    def apply(node:Option[String], items:Seq[Item]):ItemsResult =
    {
        val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
        return apply(ItemsBuilder.build(attributes, items))
    }

    def apply(xml:Node):ItemsResult = new ItemsResult(xml)
}

class ItemsResult(xml:Node) extends Query(xml)
{
    val node:Option[String] = (this.xml \ "@node").text

    val items:Seq[Item] = (this.xml \ "item").map( node => Item(node) )
}
