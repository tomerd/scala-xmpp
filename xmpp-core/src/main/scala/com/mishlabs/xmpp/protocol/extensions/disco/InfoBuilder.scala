package com.mishlabs.xmpp
package protocol
package extensions
package disco

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object InfoBuilder extends ExtensionBuilder[Query]
{
    val tag = Query.tag
    val namespace = "http://jabber.org/protocol/disco#info"

    def apply(xml:Node):Query =
    {
        (xml \ "feature").length match
        {
            case 0 => InfoRequest(xml)
            case _ => InfoResult(xml)
        }
    }
}