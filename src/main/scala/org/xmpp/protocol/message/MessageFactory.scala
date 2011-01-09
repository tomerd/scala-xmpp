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
			def create(xml:Node):Message = 
			{				
				(xml \ "@type").text match
				{					  
					// FIXME, use the enum values (attribute kind) instead of kindName, getting compilation error even with implicict cast
					case Normal.kindName => Normal(xml) 
					case Chat.kindName => Chat(xml) 
					case GroupChat.kindName => GroupChat(xml) 
					case Headline.kindName => Headline(xml) 
					case Error.kindName => Error(xml) 
					case _ => throw new Exception("unknown message stanza") // TODO, give a more detailed error message here
				}
			}
		}
	}
}