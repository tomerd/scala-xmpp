package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object ExtendedAway
		{
			def apply():ExtendedAway =
			{
				val xml = Stanza.build(Presence.TAG)
				return apply(xml)
			}
			
			def apply(xml:Node):ExtendedAway = new ExtendedAway(xml)
		}
		
		class ExtendedAway(xml:Node) extends Available(xml)
		{
		}
	}
}