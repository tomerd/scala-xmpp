package com.mishlabs.xmpp
package protocol
package extensions
package disco

import scala.collection._
import scala.xml._

import Protocol._

object InfoResult
{
    def apply(identities:Seq[Identity], features:Seq[Feature]):InfoResult = apply(None, identities, features)

    def apply(node:Option[String], identities:Seq[Identity], features:Seq[Feature]):InfoResult =
    {
        val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
        return apply(InfoBuilder.build(attributes, identities ++ features))
    }

    def apply(xml:Node):InfoResult = new InfoResult(xml)
}

class InfoResult(xml:Node) extends Query(xml)
{
    val node:Option[String] = (this.xml \ "@node").text

    val identities:Seq[Identity] = (this.xml \ "identity").map( node => Identity(node) )

    val features:Seq[Feature] = (this.xml \ "feature").map( node => Feature(node) )
}