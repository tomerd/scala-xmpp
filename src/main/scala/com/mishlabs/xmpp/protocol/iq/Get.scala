package com.mishlabs.xmpp
package protocol
package iq

import scala.collection._
import scala.xml._

import Protocol._

object Get
{
    val iqType = IQTypeEnumeration.Get

    def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Get = apply(build(id, to, from, extension))

    def apply(xml:Node):Get = new Get(xml)

    def build(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Node = IQ.build(iqType, id, to, from, extension)

    def unapply(get:Get):Option[(Option[String], Option[JID], Option[JID], Option[Extension])] = Some(get.id, get.to, get.from, get.extension)
}

class Get(xml:Node) extends IQ(xml, Get.iqType)
{
    def result(extension:Option[Extension]=None):Result = Result(this.id, this.from, this.to, extension)
}