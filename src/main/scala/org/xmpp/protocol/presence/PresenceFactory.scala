package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object PresenceFactory extends StanzaFactory[Presence]
		{
			def create(xml:Node):Option[Presence] = 
			{
				xml match
				{
					case <presence>{ content @ _* }</presence> => Some(new Error(xml))
					case _ => None
				}
			}
		}
		
	}
}