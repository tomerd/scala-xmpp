package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.iq.IQFactory
		import org.xmpp.protocol.presence.PresenceFactory
		import org.xmpp.protocol.message.MessageFactory
		
		trait StanzaFactory[T <: Stanza]  
		{
			def create(xml:Node):Option[T]
		}
		
		object StanzaFactory
		{
			// default factories
			val factories = mutable.ListBuffer[StanzaFactory[_]](IQFactory, PresenceFactory, MessageFactory)
			
			def registerFactory(factory:StanzaFactory[_])
			{
				factories += factory
			}
			
			def create(xml:Node):Stanza =
			{
				// TODO, find a more consice way for doing this
				var stanza:Stanza = null
				var factory = factories.iterator.next
				while ((null != stanza) && factories.iterator.hasNext)
				{
					stanza = factory.create(xml) match
					{
						case Some(s) => s.asInstanceOf[Stanza]
						case None => null
					}
					factory = factories.iterator.next
				}
				
				if (null == stanza) throw new Exception("no registered factory was able to create a stanza of this xml")
				return stanza
			}
			
		}
	}
}