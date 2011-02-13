package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Away
		{
			def apply():Away =
			{
				val xml = Stanza.build(Presence.tag)
				return apply(xml)
			}
			
			def apply(xml:Node):Away = new Away(xml)
		}
		
		class Away(xml:Node) extends Available(xml)
		{
		}
	}
}