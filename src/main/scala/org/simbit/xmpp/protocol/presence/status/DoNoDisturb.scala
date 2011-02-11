package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object DoNotDisturb
		{
			def apply():DoNotDisturb =
			{
				val xml = Stanza.build(Presence.tag)
				return apply(xml)
			}
			
			def apply(xml:Node):DoNotDisturb = new DoNotDisturb(xml)
		}
		
		class DoNotDisturb(xml:Node) extends Available(xml)
		{
		}
	}
}