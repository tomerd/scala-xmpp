package org.simbit.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
		
		
		object Chat
		{	
			val messageType = MessageTypeEnumeration.Chat
			val messageTypeName = messageType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(to:JID, from:JID, body:Option[String]):Chat = apply(None, to, from, None, body, None, None)
			
			def apply(id:Option[String], to:JID, from:JID, body:Option[String]):Chat = apply(id, to, from, None, body, None, None)
				
			def apply(id:Option[String], to:JID, from:JID, subject:Option[String], body:Option[String]):Chat = apply(id, to, from, subject, body, None, None)
								
			def apply(to:JID, from:JID, body:String):Chat = apply(to, from, None, Some(body))
			
			def apply(to:JID, from:JID, subject:String, body:String):Chat = apply(to, from, Some(subject), Some(body))
			
			def apply(to:JID, from:JID, subject:Option[String], body:Option[String]):Chat = apply(None, to, from, subject, body, None, None)
						
			def apply(id:Option[String], to:JID, from:JID, subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):Chat =
			{
				val xml = Message.build(messageType, id, to, from, subject, body, thread, extensions)
				return apply(xml)
			}
						
			def apply(xml:Node):Chat = new Chat(xml)
			
			def unapply(chat:Chat):Option[(Option[String], JID, JID, Option[String], Option[String], Option[String], Option[Seq[Extension]])] = Some(chat.id, chat.to, chat.from, chat.subject, chat.body, chat.thread, chat.extensions)
		}
		
		class Chat(xml:Node) extends Message(xml, Chat.messageType)
		{
		}

	}
}