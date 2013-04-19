package com.mishlabs.xmpp
package protocol
package extensions
package disco

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object ItemsBuilder extends ExtensionBuilder[Query]
{
    val tag = Query.tag
    val namespace = "http://jabber.org/protocol/disco#items"

    def apply(xml:Node):Query =
    {
        (xml \ "item").length match
        {
            case 0 => ItemsRequest(xml)
            case _ => ItemsResult(xml)
        }
    }
}
