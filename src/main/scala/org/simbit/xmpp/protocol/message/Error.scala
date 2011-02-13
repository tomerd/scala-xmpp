package org.simbit.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
		
		object Error
		{
			val messageType = MessageTypeEnumeration.Error
			val messageTypeName = messageType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(message:Message, condition:StanzaErrorCondition.Value, description:Option[String]=None):Error = apply(message.id, message.to, message.from, message.extensions, condition, description) 
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]], condition:StanzaErrorCondition.Value, description:Option[String]):Error =
			{
				val xml = Message.error(id, to, from, extensions, condition, description)
				return apply(xml)
			}
			
			def apply(xml:Node):Error = new Error(xml)
			
			def unapply(error:Error):Option[(Option[String], Option[JID], Option[JID], StanzaErrorCondition.Value, Option[String])] = Some(error.id, error.to, error.from, error.condition, error.description)
		}
		
		class Error(xml:Node) extends Message(xml, Error.messageType)
		{
			// FIXME, this should be conditional
			private val error = StanzaError((xml \ "error")(0))
			
			val errorType:Option[StanzaErrorAction.Value] = error.errorType
			
			val condition:StanzaErrorCondition.Value =  error.condition
			
			val description:Option[String] = error.description
			
			val otherConditions:Option[Seq[String]] = error.otherConditions			
		}
	}
}