package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Unavailable
		{
			def apply():Unavailable =
			{
				val xml = Stanza.build(Presence.TAG)
				return new Unavailable(xml)
			}
			
			def apply(xml:Node):Unavailable = new Unavailable(xml)
		}
		
		class Unavailable(xml:Node) extends Presence(xml)
		{
		}
	}
}