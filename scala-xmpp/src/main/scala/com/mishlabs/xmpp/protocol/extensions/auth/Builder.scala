package com.mishlabs.xmpp
package protocol
package extensions
package auth

import scala.xml._

import Protocol._

private[xmpp] object Builder extends ExtensionBuilder[Query]
{
    val tag = Query.tag
    val namespace = "jabber:iq:auth"

    def apply(xml:Node):Query = AuthenticationRequest(xml)
}




