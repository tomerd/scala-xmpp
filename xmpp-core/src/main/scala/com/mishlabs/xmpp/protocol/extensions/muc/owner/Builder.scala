package com.mishlabs.xmpp
package protocol
package extensions
package muc
package owner

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object Builder extends ExtensionBuilder[Query]
{
    val tag = Query.tag
    val namespace = com.mishlabs.xmpp.protocol.extensions.muc.general.Builder.namespace + "#owner"

    // FIXME: try to find a nicer way to do this, MUC standard is quite dirty
    def apply(xml:Node):Query =
    {
        if (1 == (xml \ Destroy.tag).length)
        {
            return Destroy(xml)
        }
        else if (1 == (xml \ forms.Builder.tag).length)
        {
            return RoomConfiguration(xml)
        }
        else
        {
            return Query(xml)
        }
    }

}