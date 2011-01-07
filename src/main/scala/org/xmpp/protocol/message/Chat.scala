package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Chat
		{
			def apply():Chat =
			{
				val xml = Stanza.build(Message.TAG)
				return new Chat(xml)
			}
			
			def apply(xml:Node):Chat = new Chat(xml)
		}
		
		class Chat(xml:Node) extends Message(xml)
		{
		}

	}
}