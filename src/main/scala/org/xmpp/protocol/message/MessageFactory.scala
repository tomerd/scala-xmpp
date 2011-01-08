package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object MessageFactory extends StanzaFactory[Message]
		{
			def create(xml:Node):Option[Message] = 
			{
				xml match
				{
					case <message>{ content @ _* }</message> => Some(Error(xml))
					case _ => None
				}
			}
			
			
		}
	}
}