package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Unsubscribed
		{
			def apply():Unsubscribed =
			{
				val xml = Stanza.build(Presence.TAG)
				return new Unsubscribed(xml)
			}
			
			def apply(xml:Node):Unsubscribed = new Unsubscribed(xml)
		}
		
		class Unsubscribed(xml:Node) extends Presence(xml)
		{
		}
	}
}