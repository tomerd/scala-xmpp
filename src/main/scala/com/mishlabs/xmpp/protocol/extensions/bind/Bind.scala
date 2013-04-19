package com.mishlabs.xmpp
package protocol
package extensions
package bind

import scala.collection._
import scala.xml._

import Protocol._

object Bind
{
    val tag = "bind"

    def apply(xml:Node):Bind = new Bind(xml)
}

class Bind(xml:Node) extends Extension(xml)
{
}

object BindRequest
{
    def apply(resource:Option[String]):BindRequest =
    {
        val children = mutable.ListBuffer[Node]()
        if (resource.isDefined) children += <resource>{ resource.get }</resource>
        val scope = new NamespaceBinding(null, BindBuilder.namespace, TopScope)
        apply(Elem(null, Bind.tag, Null, scope, children:_*))
    }

    def apply(xml:Node):BindRequest = new BindRequest(xml)
}

class BindRequest(xml:Node) extends Bind(xml)
{
    val resource:Option[String] = (this.xml \ "resource").text
}

object BindResult
{
    def apply(jid:JID):BindResult =
    {
        val children = mutable.ListBuffer[Node]()
        children += <jid>{ jid.toString }</jid>
        val scope = new NamespaceBinding(null, BindBuilder.namespace, TopScope)
        apply(Elem(null, Bind.tag, Null, scope, children:_*))
    }

    def apply(xml:Node):BindResult = new BindResult(xml)
}

class BindResult(xml:Node) extends Bind(xml)
{
    val jid:JID = (this.xml \ "jid").text
}