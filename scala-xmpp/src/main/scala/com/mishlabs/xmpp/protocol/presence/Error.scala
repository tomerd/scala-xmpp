package com.mishlabs.xmpp
package protocol
package presence

import scala.collection._
import scala.xml._

import Protocol._

object Error
{
    val presenceType = PresenceTypeEnumeration.Error

    def apply(presence:Presence, condition:StanzaErrorCondition.Value, description:Option[String]=None):Error = apply(presence.id, presence.from, presence.to, presence.extensions, condition, description)

    def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]], condition:StanzaErrorCondition.Value, description:Option[String]):Error =
    {
        val xml = Presence.error(id, to, from, extensions, condition, description)
        return apply(xml)
    }

    def apply(xml:Node):Error = new Error(xml)

    def unapply(error:Error):Option[(Option[String], Option[JID], Option[JID], StanzaErrorCondition.Value, Option[String])] = Some(error.id, error.to, error.from, error.condition, error.description)
}

class Error(xml:Node) extends Presence(xml, Error.presenceType)
{
    // FIXME, this should be conditional
    private val error = StanzaError((xml \ "error")(0))

    val errorType:Option[StanzaErrorAction.Value] = error.errorType

    val condition:StanzaErrorCondition.Value =  error.condition

    val description:Option[String] = error.description

    val otherConditions:Option[Seq[String]] = error.otherConditions
}