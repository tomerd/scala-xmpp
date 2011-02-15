package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Error
		{
			val presenceType = PresenceTypeEnumeration.Error
			val presenceTypeName = presenceType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(presence:Presence, condition:StanzaErrorCondition.Value, description:Option[String]=None):Error = apply(presence.id, presence.to, presence.from, presence.extensions, condition, description) 
			
			def apply(id:Option[String], to:JID, from:JID, extensions:Option[Seq[Extension]], condition:StanzaErrorCondition.Value, description:Option[String]):Error =
			{
				val xml = Presence.error(id, to, from, extensions, condition, description)
				return apply(xml)
			}
			
			def apply(xml:Node):Error = new Error(xml)
			
			def unapply(error:Error):Option[(Option[String], JID, JID, StanzaErrorCondition.Value, Option[String])] = Some(error.id, error.to, error.from, error.condition, error.description)
		}
		
		class Error(xml:Node) extends Presence(xml, Error.presenceType)
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