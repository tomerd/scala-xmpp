package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
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