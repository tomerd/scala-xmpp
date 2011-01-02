package org.xmpp
{
	package protocol
	{
		import scala.xml._
		
		class Message(literal:Node) extends Stanza[Message](literal)
		{
			val typeResolver = MessageTypeEnumeration
			
			final def subject:String = (this.xml \ "subject").text
			
			final def body:String = (this.xml \ "body").text
			
			final def thread:String = (this.xml \ "thread").text
		}
		
		final object MessageTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Normal = Value("normal")
			val Chat = Value("chat")
			val GroupChat = Value("groupchat")
			val Headline = Value("headline")
			val Error = Value("error")			
		}
	}
}