package com.mishlabs.xmpp
package protocol
package message

import scala.collection._
import scala.xml._

import Protocol._

private[xmpp] object Message
{
    val tag = "message"

    def apply(stanzaType:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], body:Option[String]):Message = apply(stanzaType, id, to, from, None, body, None, None)

    def apply(stanzaType:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):Message = apply(stanzaType, id, to, from, subject, body, None, None)

    def apply(stanzaType:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):Message =
    {
        val xml = build(stanzaType, id, to, from, subject, body, thread, extensions)
        return apply(xml)
    }

    def apply(xml:Node):Message = Builder(xml)

    def build(stanzaType:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):Node =
    {
        val children = mutable.ListBuffer[Node]()
        if (!subject.isEmpty) children += <subject>{ subject.get }</subject>
        if (!body.isEmpty) children += <body>{ body.get }</body>
        if (!thread.isEmpty) children += <thread>{ thread.get }</thread>
        if (!extensions.isEmpty) children ++= extensions.get

        return Stanza.build(tag, stanzaType.toString, id, to, from, children)
    }

    def error(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]], condition:StanzaErrorCondition.Value, description:Option[String]):Node =
    {
        val children = mutable.ListBuffer[Node]()
        if (!extensions.isEmpty) children ++= extensions.get
        children += StanzaError(condition, description)
        Stanza.build(tag, MessageTypeEnumeration.Error.toString, id, to, from, children)
    }
}

abstract class Message(xml:Node, messageType:MessageTypeEnumeration.Value) extends Stanza(xml)
{
    val subject:Option[String] = (this.xml \ "subject").text

    val body:Option[String] = (this.xml \ "body").text

    val thread:Option[String] = (this.xml \ "thread").text

    val extensions:Option[Seq[Extension]] = ExtensionsManager.getExtensions(this.xml)

    // FIXME, need to handle extension here
    def reply(body:Option[String]):Message = Message(this.messageType, this.id, this.from, this.to, this.subject, body, this.thread, None)

    // FIXME, need to handle extension here
    def reply(body:String):Message = Message(this.messageType, this.id, this.from, this.to, this.subject, Some(body), this.thread, None)

    // FIXME, need to handle extension here
    def reply(subject:Option[String], body:Option[String]):Message = Message(this.messageType, this.id, this.from, this.to, subject, body, this.thread, None)

    // FIXME, need to handle extension here
    def reply(subject:String, body:String):Message = Message(this.messageType, this.id, this.from, this.to, Some(subject), Some(body), this.thread, None)

    // FIXME, need to handle extension here
    def forward(to:JID):Message = Message(this.messageType, this.id, to, this.from, this.subject, this.body, this.thread, None)

    def error(condition:StanzaErrorCondition.Value, description:Option[String]):Node = Error(this, condition, description)
}

protected object MessageTypeEnumeration extends Enumeration
{
    type value = Value

    val Unknown = Value("unknown") // internal use
    val Extended = Value("") // internal use
    val Normal = Value("normal")
    val Chat = Value("chat")
    val GroupChat = Value("groupchat")
    val Headline = Value("headline")
    val Error = Value("error")

    def fromString(string:String):Value = values.find( _.toString == string ).getOrElse(Unknown)
}