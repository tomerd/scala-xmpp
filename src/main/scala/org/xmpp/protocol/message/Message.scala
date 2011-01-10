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
			val TAG = "message"
			
			def apply(kind:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], body:Option[String]):Message = apply(kind, id, to, from, None, body, None, None)
				
			def apply(kind:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):Message = apply(kind, id, to, from, subject, body, None, None)
					
			def apply(kind:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extension:Option[Extension]):Message =
			{
				val xml = build(kind, id, to, from, subject, body, thread, extension)
				return apply(xml)
			}
			
			def apply(xml:Node):Message = MessageFactory.create(xml)
						
			def build(kind:MessageTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extension:Option[Extension]):Node =
			{
				val children = mutable.ListBuffer[Node]()
				if (!subject.isEmpty) children += <subject>{ subject.get }</subject>
				if (!body.isEmpty) children += <body>{ body.get }</body>
				if (!thread.isEmpty) children += <thread>{ thread.get }</thread>
				if (!extension.isEmpty) children ++= extension.get	
				
				return Stanza.build(TAG, kind.toString, id, to, from, children)
			}
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]):Node = Stanza.error(TAG, id, to, from, condition, description)			
						
		}
		
		abstract class Message(xml:Node, val kind:MessageTypeEnumeration.Value) extends Stanza(xml)
		{			
			// getters
			val subject:Option[String] = 
			{
				val subject = (this.xml \ "subject").text
				if (subject.isEmpty) None else Some(subject)
			}
			
			val body:Option[String] = 
			{
				val body = (this.xml \ "body").text
				if (body.isEmpty) None else Some(body)
			}
			
			val thread:Option[String] = 
			{
				val thread = (this.xml \ "thread").text
				if (thread.isEmpty) None else Some(thread)
			}
			
			// FIXME, need to handle extension here
			def reply(body:String):Message = Message(this.kind, this.id, this.from, this.to, this.subject, Some(body), this.thread, None)
			
			// FIXME, need to handle extension here			
			def reply(subject:String, body:String):Message = Message(this.kind, this.id, this.from, this.to, Some(subject), Some(body), this.thread, None)
			
			// FIXME, need to handle extension here			
			def forward(to:JID):Message = Message(this.kind, this.id, to, this.from, this.subject, this.body, this.thread, None)
			
			def error(condition:ErrorCondition.Value, description:Option[String]=None):Error = Error(this.id, this.from, this.to, condition, description)
		}
		
		protected object MessageTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val Normal = Value("normal")
			val Chat = Value("chat")
			val GroupChat = Value("groupchat")
			val Headline = Value("headline")
			val Error = Value("error")
		}
		
	}
}