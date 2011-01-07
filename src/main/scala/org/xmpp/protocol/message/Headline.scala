package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Headline
		{
			def apply():Headline =
			{
				val xml = Stanza.build(Message.TAG)
				return new Headline(xml)
			}
			
			def apply(xml:Node):Headline = new Headline(xml)
		}
		
		class Headline(xml:Node) extends Message(xml)
		{
		}
		
	}
}