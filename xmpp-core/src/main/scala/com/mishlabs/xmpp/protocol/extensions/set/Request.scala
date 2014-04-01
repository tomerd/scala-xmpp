package com.mishlabs.xmpp
package protocol
package extensions
package set

import scala.collection._
import scala.xml._

import Protocol._

object Request extends ExtensionBuilder[Request]
{
    val tag = "set"
    val namespace = "http://jabber.org/protocol/rsm"

    def apply[T](max:Int, before:Option[T], after:Option[T], index:Option[Int]):Request =
    {
        val children = mutable.ListBuffer[Node]()
        children += <max>{ max }</max>
        if (!before.isEmpty) children += <before>{ before.get.toString }</before>
        if (!after.isEmpty) children += <after>{ after.get.toString }</after>
        if (!index.isEmpty) children += <index>{ index.get.toString }</index>
        apply(build(children))
    }

    def apply(xml:Node):Request = new Request(xml)
}

class Request (xml:Node) extends Extension(xml)
{
}


