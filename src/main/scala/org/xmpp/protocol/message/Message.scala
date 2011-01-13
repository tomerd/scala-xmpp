package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		protected object Message
		{
			val tag = "message"
			
			def apply(stanzaType:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], body:Option[String]):Message = apply(stanzaType, id, to, from, None, body, None, None)
				
			def apply(stanzaType:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):Message = apply(stanzaType, id, to, from, subject, body, None, None)
					
			def apply(stanzaType:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extension:Option[Extension]):Message =
			{
				val xml = build(stanzaType, id, to, from, subject, body, thread, extension)
				return apply(xml)
			}
			
			def apply(xml:Node):Message = MessageFactory.create(xml)
						
			def build(stanzaType:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extension:Option[Extension]):Node =
			{
				val children = mutable.ListBuffer[Node]()
				if (!subject.isEmpty) children += <subject>{ subject.get }</subject>
				if (!body.isEmpty) children += <body>{ body.get }</body>
				if (!thread.isEmpty) children += <thread>{ thread.get }</thread>
				if (!extension.isEmpty) children ++= extension.get	
				
				return Stanza.build(tag, stanzaType.toString, id, to, from, children)
			}
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]):Node = Stanza.error(tag, id, to, from, condition, description)						
		}
		
		abstract class Message(xml:Node, val stanzaType:MessageTypeEnumeration.Value) extends Stanza(xml)
		{			
			val subject:Option[String] = (this.xml \ "subject").text
			
			val body:Option[String] = (this.xml \ "body").text
			
			val thread:Option[String] = (this.xml \ "thread").text
			
			// FIXME, need to handle extension here
			def reply(body:String):Message = Message(this.stanzaType, this.id, this.from, this.to, this.subject, Some(body), this.thread, None)
			
			// FIXME, need to handle extension here			
			def reply(subject:String, body:String):Message = Message(this.stanzaType, this.id, this.from, this.to, Some(subject), Some(body), this.thread, None)
			
			// FIXME, need to handle extension here			
			def forward(to:JID):Message = Message(this.stanzaType, this.id, to, this.from, this.subject, this.body, this.thread, None)
			
			def error(condition:ErrorCondition.Value, description:Option[String]=None):Error = Error(this.id, this.from, this.to, condition, description)
		}
		
		protected object MessageTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val None = Value("") // internal use
			val Normal = Value("normal")
			val Chat = Value("chat")
			val GroupChat = Value("groupchat")
			val Headline = Value("headline")
			val Error = Value("error")
		}
		
	}
}