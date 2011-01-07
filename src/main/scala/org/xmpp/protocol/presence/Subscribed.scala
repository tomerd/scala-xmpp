package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Subscribed
		{
			def apply():Subscribed =
			{
				val xml = Stanza.build(Presence.TAG)
				return new Subscribed(xml)
			}
			
			def apply(xml:Node):Subscribed = new Subscribed(xml)
		}
		
		class Subscribed(xml:Node) extends Presence(xml)
		{
		}
	}
}