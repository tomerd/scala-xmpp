package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._
		
		final object Message
		{			
			val TAG = "message"
			
			def apply(id:String, to:JID, from:JID, kind:MessageTypeEnumeration.Value, body:Option[String]):Message = Message(id, to, from, kind, None, body)
				
			def apply(id:String, to:JID, from:JID, kind:MessageTypeEnumeration.Value, subject:Option[String], body:Option[String]):Message = Message(id, to, from, kind, subject, body, None)
					
			def apply(id:String, to:JID, from:JID, kind:MessageTypeEnumeration.Value, subject:Option[String], body:Option[String], thread:Option[String]):Message =
			{
				val children = mutable.ListBuffer[Node]()
				if (subject.isEmpty) children += <subject>{ subject.get }</subject>
				if (!body.isEmpty) children += <body>{ body.get }</body>
				if (!thread.isEmpty) children += <thread>{ thread.get }</thread>
				var attributes:MetaData = new UnprefixedAttribute("id", Text(id), Null)
				attributes = attributes.append(new UnprefixedAttribute("to", Text(to), Null))
				attributes = attributes.append(new UnprefixedAttribute("from", Text(from), Null))
				attributes = attributes.append(new UnprefixedAttribute("type", Text(kind.toString), Null))				
				return new Message(Elem(null, TAG, attributes, TopScope, children:_*))
			}	
								
			def error(id:String, to:JID, from:JID, errorCondition:ErrorCondition.Value):Message = error(id, to, from, errorCondition, None)
			
			def error(id:String, to:JID, from:JID, errorCondition:ErrorCondition.Value, errorDescription:Option[String]):Message =
			{
				// TODO: test this
				var attributes:MetaData = new UnprefixedAttribute("id", Text(id), Null)
				attributes = attributes.append(new UnprefixedAttribute("to", Text(to), Null))
				attributes = attributes.append(new UnprefixedAttribute("from", Text(from), Null))
				attributes = attributes.append(new UnprefixedAttribute("type", Text(MessageTypeEnumeration.Error.toString), Null))				
				return new Message(Elem(null, TAG, attributes, TopScope, Error(errorCondition, errorDescription)))				
			}
		}
		
		class Message(xml:Node) extends Stanza[Message](xml)
		{			
			val TypeEnumeration = MessageTypeEnumeration
			
			private var _subject:Option[String] = None
			private var _body:Option[String] = None
			private var _thread:Option[String] = None
			
			final def subject:String =
			{
				_subject match 
				{
					case Some(subject) => subject
					case None => _subject = Some((this.xml \ "subject").text); _subject.get
				}				
			}
			
			final def body:String =
			{
				_body match 
				{
					case Some(body) => body
					case None => _body = Some((this.xml \ "body").text); _body.get
				}				
			}
			
			final def thread:String =
			{
				_thread match 
				{
					case Some(thread) => thread
					case None => _thread = Some((this.xml \ "thread").text); _thread.get
				}				
			}
			
			final def reply(body:String):Message = reply(this.subject, body)
			
			final def reply(subject:String, body:String):Message = Message(this.id, this.from, this.to, this.kind, Some(subject), Some(body), Some(this.thread))								
			
			final def forward(to:JID):Message = Message(this.id, to, this.from, this.kind, Some(subject), Some(body), Some(this.thread))
			
			final def error(condition:ErrorCondition.Value, description:Option[String]=None) = Message.error(this.id, this.from, this.to, condition, description)
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