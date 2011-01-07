package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Message
		{
			val TAG = "message"
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[MessageTypeEnumeration.Value], body:Option[String]):Message = apply(id, to, from, kind, None, body, None, None)
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[MessageTypeEnumeration.Value], subject:Option[String], body:Option[String]):Message = apply(id, to, from, kind, subject, body, None, None)
					
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[MessageTypeEnumeration.Value], subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):Message =
			{
				val children = mutable.ListBuffer[Node]()
				if (!subject.isEmpty) children += <subject>{ subject.get }</subject>
				if (!body.isEmpty) children += <body>{ body.get }</body>
				if (!thread.isEmpty) children += <thread>{ thread.get }</thread>
				if (!extensions.isEmpty) children ++= extensions.get	
				
				val xml = Stanza.build(TAG, id, to, from, kind, children)
				return apply(xml)
			}
			
			def apply(xml:Node):Message = MessageFactory.create(xml).get
			
			/*			
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]=None):Message =
			{
				val xml = Stanza.error(TAG, id, to, from, condition, description)
				return new Message(xml)
			}
			*/			
		}
		
		abstract class Message(xml:Node) extends Stanza(xml)
		{			
			val TypeEnumeration = MessageTypeEnumeration
			
			// getters
			private var _subject:Option[String] = None
			def subject:Option[String] = _subject
			
			private var _body:Option[String] = None
			def body:Option[String] = _body
			
			private var _thread:Option[String] = None
			def thread:Option[String] = _thread
			
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
			
			def reply(body:String):Message = Message(this.id, this.from, this.to, this.kind, this.subject, Some(body), this.thread, None)
			
			def reply(subject:String, body:String):Message = Message(this.id, this.from, this.to, this.kind, Some(subject), Some(body), this.thread, None)
			
			def forward(to:JID):Message = Message(this.id, to, this.from, this.kind, this.subject, this.body, this.thread, None)
			
			def error(condition:ErrorCondition.Value, description:Option[String]=None):Error = Error(this.id, this.from, this.to, condition, description)
		}
		
		object MessageTypeEnumeration extends Enumeration
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