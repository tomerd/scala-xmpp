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
		
		object EventBuilder extends ExtensionBuilder[Event]
		{
			val name = Event.name
			val namespace = Builder.namespace + "#event"
			
			// FIXME: implement this
			def apply(xml:Node):Event = Event(xml)
			
		}
		
	}
}
