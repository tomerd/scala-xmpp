package com.mishlabs.xmpp
package protocol
package extensions
package disco

import scala.collection._
import scala.xml._

import Protocol._

object Feature
{
    val tag = "feature"

    def apply(identifier:String):Feature =
    {
        val metadata = new UnprefixedAttribute("var", Text(identifier), Null)
        return apply(Elem(null, tag, metadata, TopScope))
    }

    def apply(xml:Node):Feature = new Feature(xml)
}

class Feature(xml:Node) extends XmlWrapper(xml)
{
    val identifier:String = (this.xml \ "@var").text
}
