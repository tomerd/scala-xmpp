package com.mishlabs.xmpp
package protocol
package extensions
package bind

import scala.collection._
import scala.xml._

import Protocol._

object Unbind
{
    val tag = "ubbind"

    def apply(xml:Node):Unbind = new Unbind(xml)
}

class Unbind(xml:Node) extends Extension(xml)
{
}

object UnbindRequest
{
    def apply(resource:String):UnbindRequest =
    {
        val children = mutable.ListBuffer[Node]()
        children += <resource>{ resource }</resource>
        val scope = new NamespaceBinding(null, BindBuilder.namespace, TopScope)
        apply(Elem(null, Unbind.tag, Null, scope, children:_*))
    }

    def apply(xml:Node):UnbindRequest = new UnbindRequest(xml)
}

class UnbindRequest(xml:Node) extends Unbind(xml)
{
    val resource:String = (this.xml \ "resource").text
}