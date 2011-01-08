package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object DoNotDisturb
		{
			def apply():DoNotDisturb =
			{
				val xml = Stanza.build(Presence.TAG)
				return apply(xml)
			}
			
			def apply(xml:Node):DoNotDisturb = new DoNotDisturb(xml)
		}
		
		class DoNotDisturb(xml:Node) extends Available(xml)
		{
		}
	}
}