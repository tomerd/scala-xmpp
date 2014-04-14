package com.mishlabs.xmpp
package protocol
package iq

import scala.collection._
import scala.xml._

import Protocol._

object Error
{
    val iqType = IQTypeEnumeration.Error

    def apply(iq:IQ, condition:StanzaErrorCondition.Value, description:Option[String]=None):Error = apply(iq.id, iq.from, iq.to, iq.extension, condition, description)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension], condition:StanzaErrorCondition.Value, description:Option[String]):Error =
    {
        val xml = IQ.error(id, to, from, extension, condition, description)
        apply(xml)
    }

    def apply(xml:Node):Error = new Error(xml)

    def unapply(error:Error):Option[(Option[String], Option[JID], Option[JID], Option[StanzaErrorCondition.Value], Option[String])] = Some(error.id, error.to, error.from, error.condition, error.description)
}

class Error(xml:Node) extends IQ(xml, Error.iqType)
{
    private val error = (xml \ "error").length match
    {
        case 1 => Some(StanzaError((xml \ "error")(0)))
        case _ => None
    }

    val errorType:Option[StanzaErrorAction.Value] = if (error.isDefined) error.get.errorType else None

    val condition:Option[StanzaErrorCondition.Value] =  if (error.isDefined) Some(error.get.condition) else None

    val description:Option[String] = if (error.isDefined) error.get.description else None

    val otherConditions:Option[Seq[String]] =if (error.isDefined) error.get.otherConditions else None
}
