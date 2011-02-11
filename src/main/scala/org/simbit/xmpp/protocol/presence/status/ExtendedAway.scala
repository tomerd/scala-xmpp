package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object ExtendedAway
		{
			def apply():ExtendedAway =
			{
				val xml = Stanza.build(Presence.tag)
				return apply(xml)
			}
			
			def apply(xml:Node):ExtendedAway = new ExtendedAway(xml)
		}
		
		class ExtendedAway(xml:Node) extends Available(xml)
		{
		}
	}
}