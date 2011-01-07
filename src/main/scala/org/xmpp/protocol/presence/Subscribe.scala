package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Subscribe
		{
			def apply():Subscribe =
			{
				val xml = Stanza.build(Presence.TAG)
				return new Subscribe(xml)
			}
			
			def apply(xml:Node):Subscribe = new Subscribe(xml)
		}
		
		class Subscribe(xml:Node) extends Presence(xml)
		{
		}
	}
}