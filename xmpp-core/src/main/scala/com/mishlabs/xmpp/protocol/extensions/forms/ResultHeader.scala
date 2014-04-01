package com.mishlabs.xmpp
package protocol
package extensions
package forms

import scala.collection._
import scala.xml._

import fields._

import Protocol._

object ResultHeader
{
    val tag = "reported"

    def apply(fields:Seq[Node]):ResultHeader = apply(build(fields))

    def apply(xml:Node):ResultHeader = new ResultHeader(xml)

    def build(fields:Seq[Node]):Node =
    {
        Elem(null, tag, Null, TopScope, fields:_*)
    }
}

class ResultHeader(xml:Node) extends XmlWrapper(xml)
{
}