package com.mishlabs.xmpp
package protocol
package extensions
package rpc

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object Builder extends ExtensionBuilder[Query]
{
    val tag = Query.tag
    val namespace = "jabber:iq:rpc"

    def apply(xml:Node):Query =
    {
        if (1 == (xml \ MethodCall.tag).length)
        {
            return MethodCall(xml)
        }
        else if (1 == (xml \ MethodResponse.tag).length)
        {
            return MethodResponse(xml)
        }
        else
        {
            return Query(xml)
        }
    }
}

