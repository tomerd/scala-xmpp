package org.simbit.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		private[xmpp] object Builder
		{
			def apply(xml:Node):Message = 
			{
				require("message" == xml.label)
				
				(xml \ "@type").text match
				{
					// FIXME, use the enum values (attribute stanzaType) instead of messageTypeName, getting compilation error even with implicict cast
					case Normal.messageTypeName => Normal(xml) 
					case Chat.messageTypeName => Chat(xml) 
					case GroupChat.messageTypeName => GroupChat(xml) 
					case Headline.messageTypeName => Headline(xml) 
					case Error.messageTypeName => Error(xml) 
					case _ => throw new Exception("unknown message stanza") // TODO, give a more detailed error message here
				}
			}
		}
	}
}