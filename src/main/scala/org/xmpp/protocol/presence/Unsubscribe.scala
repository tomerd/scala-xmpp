package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Unsubscribe
		{
			def apply():Unsubscribe =
			{
				val xml = Stanza.build(Presence.TAG)
				return new Unsubscribe(xml)
			}
			
			def apply(xml:Node):Unsubscribe = new Unsubscribe(xml)
		}
		
		class Unsubscribe(xml:Node) extends Presence(xml)
		{
		}
	}
}