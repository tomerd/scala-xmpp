package org.xmpp
{
	package protocol.extensions.pubsub
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.message._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object Builder extends ExtensionBuilder[Pubsub]
		{
			val name = Pubsub.name
			val namespace = "http://jabber.org/protocol/pubsub"
			
			// FIXME: implement this
			def apply(xml:Node):Pubsub = Pubsub(xml)
			
		}
		
	}
}
