package com.mishlabs.xmpp
package protocol
package extensions
package bind

import scala.xml._

private[xmpp] object UnbindBuilder extends ExtensionBuilder[Unbind]
{
    val tag = Unbind.tag
    val namespace = "urn:ietf:params:xml:ns:xmpp-bind"

    def apply(xml:Node):Unbind = UnbindRequest(xml)
}

