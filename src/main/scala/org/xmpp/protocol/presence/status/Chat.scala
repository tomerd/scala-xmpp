package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Chat
		{
			def apply():Chat =
			{
				val xml = Stanza.build(Presence.TAG)
				return apply(xml)
			}
			
			def apply(xml:Node):Chat = new Chat(xml)
		}
		
		class Chat(xml:Node) extends Available(xml)
		{
		}
	}
}