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
			
			def apply(iq:IQ, condition:StanzaErrorCondition.Value, description:Option[String]=None):Error = apply(iq.id, iq.from, iq.to, iq.extension, condition, description) 
			
			def apply(id:Option[String], to:JID, from:JID, extension:Option[Extension], condition:StanzaErrorCondition.Value, description:Option[String]):Error =
			{
				val xml = IQ.error(id, to, from, extension, condition, description)
				return apply(xml)
			}
			
			def apply(xml:Node):Error = new Error(xml)
			
			def unapply(error:Error):Option[(Option[String], JID, JID, StanzaErrorCondition.Value, Option[String])] = Some(error.id, error.to, error.from, error.condition, error.description)
		}
		
		class Error(xml:Node) extends IQ(xml, Error.iqType)
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