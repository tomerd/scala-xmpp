package com.mishlabs.xmpp
package protocol
package extensions
package register

import scala.xml._

private[xmpp] object Builder extends ExtensionBuilder[Query]
{
    val tag = Query.tag
    val namespace = "jabber:iq:register"

    def apply(xml:Node):Query = RegistrationRequest(xml)
}




