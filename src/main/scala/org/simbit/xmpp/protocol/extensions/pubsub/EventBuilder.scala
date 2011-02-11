package org.simbit.xmpp
{
	package protocol.extensions.pubsub
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.message._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object EventBuilder extends ExtensionBuilder[Event]
		{
			val name = Event.name
			val namespace = Builder.namespace + "#event"
			
			// FIXME: implement this
			def apply(xml:Node):Event = Event(xml)
			
		}
		
	}
}
