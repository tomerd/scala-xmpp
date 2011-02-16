package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
					
		private[xmpp] object Presence
		{
			val tag = "presence"
				
			def build(presenceType:PresenceTypeEnumeration.Value, id:Option[String], to:JID, from:JID):Node = build(presenceType, id, to, from, None, None)	
				
			def build(presenceType:PresenceTypeEnumeration.Value, id:Option[String], to:JID, from:JID, extensions:Option[Seq[Extension]]):Node = build(presenceType, id, to, from, None, extensions)		
			
			def build(presenceType:PresenceTypeEnumeration.Value, id:Option[String], to:JID, from:JID, children:Option[Seq[Node]], extensions:Option[Seq[Extension]]):Node =
			{
				val kids = mutable.ListBuffer[Node]()
				if (!children.isEmpty) kids ++= children.get
				if (!extensions.isEmpty) kids ++= extensions.get
					
				return Stanza.build(tag, presenceType.toString, id, to, from, kids)				
			}
			
			def error(id:Option[String], to:JID, from:JID, extensions:Option[Seq[Extension]], condition:StanzaErrorCondition.Value, description:Option[String]):Node = 
			{
				val children = mutable.ListBuffer[Node]()
				if (!extensions.isEmpty) children ++= extensions.get
				children += StanzaError(condition, description)
				Stanza.build(tag, PresenceTypeEnumeration.Error.toString, id, to, from, children)
			}
		}
		
		abstract class Presence(xml:Node, presenceType:PresenceTypeEnumeration.Value) extends Stanza(xml)
		{
			val extensions:Option[Seq[Extension]] = ExtensionsManager.getExtensions(this.xml)
			
			def error(condition:StanzaErrorCondition.Value, description:Option[String]):Node = Error(this, condition, description)
		}
		
		protected  object PresenceTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val Available = Value("") // TODO, check this
			val Unavailable = Value("unavailable")
			val Subscribe = Value("subscribe")
			val Subscribed = Value("subscribed")
			val Unsubscribe = Value("unsubscribe")
			val Unsubscribed = Value("unsubscribed")
			val Probe = Value("probe")
			val Error = Value("error")
		}
		
	}
}