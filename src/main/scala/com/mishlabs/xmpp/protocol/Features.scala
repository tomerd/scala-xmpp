package com.mishlabs.xmpp
package protocol

import scala.collection._
import scala.xml._

object Features
{
    val tag = "stream:features"

    def apply(features:Seq[Node]):Features =
    {
        apply(Elem(null, tag, Null, TopScope, features:_*))
    }

    def apply(xml:Node):Features = new Features(xml)
}

class Features(xml:Node) extends XmlWrapper(xml) with Packet
{
    val features:Seq[Node] =  this.xml.child
}