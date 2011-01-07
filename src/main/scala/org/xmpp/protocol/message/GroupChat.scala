package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object GroupChat
		{
			def apply():GroupChat =
			{
				val xml = Stanza.build(Message.TAG)
				return new GroupChat(xml)
			}
			
			def apply(xml:Node):GroupChat = new GroupChat(xml)
		}
		
		class GroupChat(xml:Node) extends Message(xml)
		{
		}
	}
}