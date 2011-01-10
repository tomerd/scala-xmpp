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
				require("message" == xml.label)
				
				(xml \ "@type").text match
				{					  
					// FIXME, use the enum values (attribute stanzaType) instead of stanzaTypeName, getting compilation error even with implicict cast
					case Normal.stanzaTypeName => Normal(xml) 
					case Chat.stanzaTypeName => Chat(xml) 
					case GroupChat.stanzaTypeName => GroupChat(xml) 
					case Headline.stanzaTypeName => Headline(xml) 
					case Error.stanzaTypeName => Error(xml) 
					case _ => throw new Exception("unknown message stanza") // TODO, give a more detailed error message here
				}
			}
		}
	}
}