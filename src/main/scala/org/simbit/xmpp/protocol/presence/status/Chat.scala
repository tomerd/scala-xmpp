package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Chat
		{
			def apply():Chat =
			{
				val xml = Stanza.build(Presence.tag)
				return apply(xml)
			}
			
			def apply(xml:Node):Chat = new Chat(xml)
		}
		
		class Chat(xml:Node) extends Available(xml)
		{
		}
	}
}