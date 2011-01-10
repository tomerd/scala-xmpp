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
			val stanzaType = MessageTypeEnumeration.Error
			val stanzaTypeName = stanzaType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]=None):Error =
			{
				val xml = Message.error(id, to, from, condition, description)
				return apply(xml)
			}
			
			def apply(xml:Node):Error = new Error(xml)
		}
		
		class Error(xml:Node) extends Message(xml, Error.stanzaType)
		{
		}
	}
}