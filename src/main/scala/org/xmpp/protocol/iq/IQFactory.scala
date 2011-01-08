package org.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object IQFactory extends StanzaFactory[IQ]
		{
			def create(xml:Node):Option[IQ] =
			{
				xml match
				{
					case <iq>{ content @ _* }</iq> => Some(Error(xml))					
					case _ => None
				}				
			}
		}
		
	}
}
