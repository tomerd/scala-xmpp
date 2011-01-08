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
					// FIXME, handle other cases here
					case root @ <message>{ extensions @ _* }</message> if (root \ "@type").text == MessageTypeEnumeration.Normal.toString => Some(Normal(root))
					case root @ <message>{ extensions @ _* }</message> if (root \ "@type").text == MessageTypeEnumeration.Chat.toString => Some(Chat(root))
					case root @ <message>{ extensions @ _* }</message> if (root \ "@type").text == MessageTypeEnumeration.GroupChat.toString => Some(GroupChat(root))
					case root @ <message>{ extensions @ _* }</message> if (root \ "@type").text == MessageTypeEnumeration.Headline.toString => Some(Headline(root))
					case root @ <message>{ extensions @ _* }</message> if (root \ "@type").text == MessageTypeEnumeration.Error.toString => Some(Error(root))								
					case _ => None
				}
			}
			
			
		}
	}
}