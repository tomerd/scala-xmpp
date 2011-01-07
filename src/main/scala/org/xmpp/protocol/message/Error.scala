package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Error
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]=None):Error =
			{
				val xml = Stanza.error(Message.TAG, id, to, from, condition, description)
				return new Error(xml)
			}
			
			def apply(xml:Node):Error = new Error(xml)
		}
		
		class Error(xml:Node) extends Message(xml)
		{
		}
	}
}