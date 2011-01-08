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
				val iterator = factories.iterator
				while (iterator.hasNext)
				{
					iterator.next.create(xml) match
					{
						case Some(s) => return s.asInstanceOf[Stanza]
						case None => // continue
					}
				}
				
				throw new Exception("no registered factory was able to create a stanza of this xml")
			}
			
		}
	}
}