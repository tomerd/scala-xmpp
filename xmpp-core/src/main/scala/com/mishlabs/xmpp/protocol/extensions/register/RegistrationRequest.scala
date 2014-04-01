package com.mishlabs.xmpp
package protocol
package extensions
package register

import scala.collection._
import scala.xml._

object RegistrationRequest
{
    def apply(username:String, password:String, email:String):RegistrationRequest =
    {
        val children = mutable.ListBuffer[Node]()
        children += <username>{ username }</username>
        children += <password>{ password }</password>
        children += <email>{ email }</email>
        val scope = new NamespaceBinding(null, Builder.namespace, TopScope)
        apply(Elem(null, Query.tag, Null, scope, children:_*))
    }

    def apply(xml:Node):RegistrationRequest = new RegistrationRequest(xml)
}

class RegistrationRequest(xml:Node) extends Query(xml)
{
    val username:String = (this.xml \ "username").text
    val password:String = (this.xml \ "password").text
    val email:String = (this.xml \ "email").text
}