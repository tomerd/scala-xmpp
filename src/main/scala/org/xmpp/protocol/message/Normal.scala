package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
				
		object Normal
		{
			def apply():Normal =
			{
				val xml = Stanza.build(Message.TAG)
				return new Normal(xml)
			}
			
			def apply(xml:Node):Normal = new Normal(xml)
		}
		
		class Normal(xml:Node) extends Message(xml)
		{
		}
		
	}
}