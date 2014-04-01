package com.mishlabs.xmpp
package protocol
package extensions
package muc
package owner

import scala.collection._
import scala.xml._

import forms._

import Protocol._

object RoomConfiguration
{
    def apply(form:Form):RoomConfiguration =
    {
        return apply(Builder.build(form))
    }

    def apply(xml:Node):RoomConfiguration = RoomConfiguration(xml)
}

class RoomConfiguration(xml:Node) extends Query(xml)
{
    val form:Form = forms.Builder(xml.child(0))
}