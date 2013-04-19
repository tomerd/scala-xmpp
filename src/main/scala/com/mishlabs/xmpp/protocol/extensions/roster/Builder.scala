package com.mishlabs.xmpp
package protocol
package extensions
package roster

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object Builder extends ExtensionBuilder[Query]
{
    val tag = Query.tag
    val namespace = "jabber:iq:roster"

    def apply(xml:Node):Query =
    {
        (xml \ "item").length match
        {
            case 0 => RosterRequest(xml)
            case _ => RosterResult(xml)
        }
    }
}