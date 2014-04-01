package com.mishlabs.xmpp
package protocol
package extensions
package auth

import scala.collection._
import scala.xml._

import Protocol._

object AuthenticationRequest
{
    def apply(username:String, resource:String, password:Option[String], digest:Option[String]):AuthenticationRequest =
    {
        val children = mutable.ListBuffer[Node]()
        children += <username>{ username }</username>
        children += <resource>{ resource }</resource>
        if (password.isDefined) children += <password>{ password.get }</password>
        if (digest.isDefined) children += <digest>{ digest.get }</digest>
        val scope = new NamespaceBinding(null, Builder.namespace, TopScope)
        apply(Elem(null, Query.tag, Null, scope, children:_*))
    }

    def apply(xml:Node):AuthenticationRequest = new AuthenticationRequest(xml)
}

class AuthenticationRequest(xml:Node) extends Query(xml)
{
    val username:String = (this.xml \ "username").text
    val resource:String = (this.xml \ "resource").text
    val password:Option[String] = (this.xml \ "password").text
    val digest:Option[String] = (this.xml \ "digest").text
}