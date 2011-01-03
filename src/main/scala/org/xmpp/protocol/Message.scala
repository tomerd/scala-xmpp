package org.xmpp
{
	package protocol
	{
		import scala.xml._
		import scala.collection._
		
		import org.xmpp.protocol.Protocol._
		
		final object Message
		{
			def construct(id:String, to:JID, from:JID, kind:MessageTypeEnumeration.Value, subject:String ,body:String, thread:String):Message =
			{
				val children = mutable.ListBuffer[Node]()
				if ((null != subject) && (!subject.isEmpty)) children += <subject>{ subject }</subject>
				if ((null != body) && (!body.isEmpty)) children += <body>{ body }</body>
				if ((null != thread) && (!thread.isEmpty)) children += <thread>{ thread }</thread>
				var attributes:MetaData = new UnprefixedAttribute("id", Text(id), Null)
				attributes = attributes.append(new UnprefixedAttribute("to", Text(to), Null))
				attributes = attributes.append(new UnprefixedAttribute("from", Text(from), Null))
				attributes = attributes.append(new UnprefixedAttribute("type", Text(kind.toString), Null))				
				return new Message(Elem(null, "message", attributes, TopScope, children:_*))
			}			
		}
		
		class Message(literal:Node) extends Stanza[Message](literal)
		{			
			val TypeEnumeration = MessageTypeEnumeration
			
			final def subject:String = (this.xml \ "subject").text
			
			final def body:String = (this.xml \ "body").text
			
			final def thread:String = (this.xml \ "thread").text
			
			final def reply(body:String):Message = reply(this.subject, body)
			
			final def reply(subject:String, body:String):Message = reply(this.from, subject, body)
						
			final def reply(to:JID, body:String):Message = reply(to, this.subject, body)
			
			final def reply(to:JID, subject:String, body:String):Message = Message.construct(this.id, to, this.to, this.kind, subject, body, this.thread)
		}
		
		final object MessageTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Normal = Value("normal")
			val Chat = Value("chat")
			val GroupChat = Value("groupchat")
			val Headline = Value("headline")
			val Error = Value("error")			
		}
	}
}