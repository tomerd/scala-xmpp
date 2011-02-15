package org.simbit.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
		
		object GroupChat
		{
			val messageType = MessageTypeEnumeration.GroupChat
			val messageTypeName = messageType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(to:JID, from:JID, body:Option[String]):GroupChat = apply(None, to, from, None, body, None, None)
			
			def apply(id:Option[String], to:JID, from:JID, body:Option[String]):GroupChat = apply(id, to, from, None, body, None, None)
				
			def apply(id:Option[String], to:JID, from:JID, subject:Option[String], body:Option[String]):GroupChat = apply(id, to, from, subject, body, None, None)
					
			def apply(id:Option[String], to:JID, from:JID, subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):GroupChat =
			{
				val xml = Message.build(messageType, id, to, from, subject, body, thread, extensions)
				return apply(xml)
			}
			
			def apply(xml:Node):GroupChat = new GroupChat(xml)
			
			def unapply(groupchat:GroupChat):Option[(Option[String], JID, JID, Option[String], Option[String], Option[String], Option[Seq[Extension]])] = Some(groupchat.id, groupchat.to, groupchat.from, groupchat.subject, groupchat.body, groupchat.thread, groupchat.extensions)
		}
		
		class GroupChat(xml:Node) extends Message(xml, GroupChat.messageType)
		{
		}
	}
}