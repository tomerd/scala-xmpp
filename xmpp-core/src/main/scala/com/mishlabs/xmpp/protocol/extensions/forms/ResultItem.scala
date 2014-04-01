package com.mishlabs.xmpp
package protocol
package extensions
package forms

import scala.collection._
import scala.xml._

import fields._

import Protocol._

object ResultItem
{
    val tag = "item"

    def apply(fields:Seq[Node]):ResultItem = apply(build(fields))

    def apply(xml:Node):ResultItem = new ResultItem(xml)

    def build(fields:Seq[Node]):Node =
    {
        Elem(null, tag, Null, TopScope, fields:_*)
    }
}

class ResultItem(xml:Node) extends XmlWrapper(xml)
{
}