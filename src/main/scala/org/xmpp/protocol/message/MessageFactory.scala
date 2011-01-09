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
					// FIXME, use enum values instead of hard coded string here (getting compilation error. even with implicict cast)
					case "normal" => Normal(xml) // MessageTypeEnumeration.Normal		
					case "chat" => Chat(xml) // MessageTypeEnumeration.Chat
					case "groupchat" => GroupChat(xml) // MessageTypeEnumeration.GroupChat				
					case "headline" => Headline(xml) // MessageTypeEnumeration.Headline
					case "error" => Error(xml) // MessageTypeEnumeration.Error
					case _ => throw new Exception("unknown message stanza") // TODO, give a more detailed error message here
				}			
			}
		}
	}
}