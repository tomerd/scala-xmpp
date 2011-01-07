package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Probe
		{
			def apply():Probe =
			{
				val xml = Stanza.build(Presence.TAG)
				return new Probe(xml)
			}
			
			def apply(xml:Node):Probe = new Probe(xml)
		}
		
		class Probe(xml:Node) extends Presence(xml)
		{
		}
	}
}