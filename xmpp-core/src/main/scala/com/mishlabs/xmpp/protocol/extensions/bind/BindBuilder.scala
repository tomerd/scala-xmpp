package com.mishlabs.xmpp
package protocol
package extensions
package bind

import scala.xml._

private[xmpp] object BindBuilder extends ExtensionBuilder[Bind]
{
    val tag = Bind.tag
    val namespace = "urn:ietf:params:xml:ns:xmpp-bind"

    def apply(xml:Node):Bind = (xml \ "jid").length match
    {
        case 1 => BindResult(xml)
        case _ => BindRequest(xml)
    }
}

