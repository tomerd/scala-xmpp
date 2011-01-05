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
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[MessageTypeEnumeration.Value], body:Option[String]):Message = Message(id, to, from, kind, None, body, None, None)
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[MessageTypeEnumeration.Value], subject:Option[String], body:Option[String]):Message = Message(id, to, from, kind, subject, body, None, None)
					
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[MessageTypeEnumeration.Value], subject:Option[String], body:Option[String], thread:Option[String], extension:Option[Extension]):Message =
			{
				val children = mutable.ListBuffer[Node]()
				if (!subject.isEmpty) children += <subject>{ subject.get }</subject>
				if (!body.isEmpty) children += <body>{ body.get }</body>
				if (!thread.isEmpty) children += <thread>{ thread.get }</thread>
				if (!extension.isEmpty) children += extension.get
				var metadata:MetaData = Null
				if (!id.isEmpty) metadata = metadata.append(new UnprefixedAttribute("id", Text(id.get), Null))
				if (!to.isEmpty) metadata = metadata.append(new UnprefixedAttribute("to", Text(to.get), Null))
				if (!from.isEmpty) metadata = metadata.append(new UnprefixedAttribute("from", Text(from.get), Null))
				if (!kind.isEmpty) metadata = metadata.append(new UnprefixedAttribute("type", Text(kind.get.toString), Null))				
				return new Message(Elem(null, TAG, metadata, TopScope, children:_*))
			}	
											
			def error(id:Option[String], to:Option[JID], from:Option[JID], errorCondition:ErrorCondition.Value, errorDescription:Option[String]=None):Message =
			{
				// TODO: test this
				var metadata:MetaData = new UnprefixedAttribute("type", Text(MessageTypeEnumeration.Error.toString), Null)
				if (!id.isEmpty) metadata = metadata.append(new UnprefixedAttribute("id", Text(id.get), Null))
				if (!to.isEmpty) metadata = metadata.append(new UnprefixedAttribute("to", Text(to.get), Null))
				if (!from.isEmpty) metadata = metadata.append(new UnprefixedAttribute("from", Text(from.get), Null))								
				return new Message(Elem(null, TAG, metadata, TopScope, Error(errorCondition, errorDescription)))				
			}
		}
		
		class Message(xml:Node) extends Stanza[Message](xml)
		{			
			val TypeEnumeration = MessageTypeEnumeration
			
			// getters
			private var _subject:Option[String] = None
			final def subject:Option[String] = _subject
			
			private var _body:Option[String] = None
			final def body:Option[String] = _body
			
			private var _thread:Option[String] = None
			final def thread:Option[String] = _thread
			
			override protected def parse
			{
				super.parse
				
				val subject = (this.xml \ "subject").text
				_subject = if (subject.isEmpty) None else Some(subject)
				
				val body = (this.xml \ "body").text
				_body = if (body.isEmpty) None else Some(body)
				
				val thread = (this.xml \ "thread").text
				_thread = if (thread.isEmpty) None else Some(thread)				
			}
			
			final def reply(body:String):Message = Message(this.id, this.from, this.to, this.kind, this.subject, Some(body), this.thread, None)
			
			final def reply(subject:String, body:String):Message = Message(this.id, this.from, this.to, this.kind, Some(subject), Some(body), this.thread, None)								
			
			final def forward(to:JID):Message = Message(this.id, to, this.from, this.kind, this.subject, this.body, this.thread, None)
			
			final def error(condition:ErrorCondition.Value, description:Option[String]=None):Message = Message.error(this.id, this.from, this.to, condition, description)
		}
		
		final object MessageTypeEnumeration extends Enumeration
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