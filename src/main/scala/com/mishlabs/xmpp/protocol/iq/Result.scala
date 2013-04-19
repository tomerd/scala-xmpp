package com.mishlabs.xmpp
package protocol
package iq

import scala.collection._
import scala.xml._

import Protocol._

object Result
{
    val iqType = IQTypeEnumeration.Result

    def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Result = apply(build(id, to, from, extension))

    def apply(iq:IQ):Result = apply(iq, None)

    def apply(iq:IQ, extension:Option[Extension]):Result = apply(build(iq.id, iq.from, iq.to, extension))

    def apply(xml:Node):Result = new Result(xml)

    def build(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Node = IQ.build(iqType, id, to, from, extension)

    def unapply(result:Result):Option[(Option[String], Option[JID], Option[JID], Option[Extension])] = Some(result.id, result.to, result.from, result.extension)
}

class Result(xml:Node) extends IQ(xml, Result.iqType)
{
}