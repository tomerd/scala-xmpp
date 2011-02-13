package org.simbit.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
		
		object Error
		{
			val iqType = IQTypeEnumeration.Error
			val iqTypeName = iqType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(iq:IQ, condition:ErrorCondition.Value, description:Option[String]=None):Error = apply(iq.id, iq.to, iq.from, iq.extension, condition, description) 
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension], condition:ErrorCondition.Value, description:Option[String]):Error =
			{
				val xml = IQ.error(id, to, from, extension, condition, description)
				return apply(xml)
			}
			
			def apply(xml:Node):Error = new Error(xml)
			
			def unapply(error:Error):Option[(Option[String], Option[JID], Option[JID], ErrorCondition.Value, Option[String])] = Some(error.id, error.to, error.from, error.condition, error.description)
		}
		
		class Error(xml:Node) extends IQ(xml, Error.iqType)
		{
			// FIXME, this should be conditional
			private val error = StanzaError((xml \ "error")(0))
			
			val errorType:Option[ErrorType.Value] = error.errorType
			
			val condition:ErrorCondition.Value =  error.condition
			
			val description:Option[String] = error.description
			
			val otherConditions:Option[Seq[String]] = error.otherConditions
		}
	}
}