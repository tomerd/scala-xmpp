package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
					
		protected[xmpp] object Presence
		{
			val tag = "presence"
			
			/*
			def build(presenceType:PresenceTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], show:Option[Show.Value], status:Option[String], priority:Option[Int], extension:Option[Extension]):Node =
			{
				val children = mutable.ListBuffer[Node]()
				if (!show.isEmpty) children += <show>{ show.get }</show>
				if (!status.isEmpty) children += <status>{ status.get }</status>
				if (!priority.isEmpty) children += <priority>{ priority.get }</priority>
				if (!extension.isEmpty) children ++= extension.get
					
				return Stanza.build(tag, presenceType.toString, id, to, from, children)
			}
			*/
			
			def build(presenceType:PresenceTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID]):Node = build(presenceType, id, to, from, None, None)	
				
			def build(presenceType:PresenceTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]):Node = build(presenceType, id, to, from, None, extension)		
			
			def build(presenceType:PresenceTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], children:Option[Seq[Node]], extension:Option[Extension]):Node =
			{
				val kids = mutable.ListBuffer[Node]()
				if (!children.isEmpty) kids ++= children.get
				if (!extension.isEmpty) kids ++= extension.get
					
				return Stanza.build(tag, presenceType.toString, id, to, from, kids)				
			}
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]):Node = Stanza.error(tag, id, to, from, condition, description)
		}
		
		abstract class Presence(xml:Node, val presenceType:PresenceTypeEnumeration.Value) extends Stanza(xml)
		{
			def error(condition:ErrorCondition.Value, description:Option[String]=None):Error = Error(this.id, this.from, this.to, condition, description)
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