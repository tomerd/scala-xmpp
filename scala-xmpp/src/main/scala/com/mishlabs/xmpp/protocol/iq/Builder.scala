package com.mishlabs.xmpp
package protocol
package iq

import scala.xml._

import Protocol._

private[xmpp] object Builder
{
    def apply(xml:Node):IQ =
    {
        require("iq" == xml.label)

        IQTypeEnumeration.fromString((xml \ "@type").text) match
        {
            case IQTypeEnumeration.Get => Get(xml)
            case IQTypeEnumeration.Set => iq.Set(xml)
            case IQTypeEnumeration.Result => Result(xml)
            case IQTypeEnumeration.Error => Error(xml)
            case _ => throw new Exception("unknown iq stanza") // TODO, give a more detailed error message here
        }
    }
}