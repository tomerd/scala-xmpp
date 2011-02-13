package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
					
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
				
			def build(presenceType:PresenceTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Node = build(presenceType, id, to, from, None, extensions)		
			
			def build(presenceType:PresenceTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], children:Option[Seq[Node]], extensions:Option[Seq[Extension]]):Node =
			{
				val kids = mutable.ListBuffer[Node]()
				if (!children.isEmpty) kids ++= children.get
				if (!extensions.isEmpty) kids ++= extensions.get
					
				return Stanza.build(tag, presenceType.toString, id, to, from, kids)				
			}
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]], condition:ErrorCondition.Value, description:Option[String]):Node = 
			{
				val children = mutable.ListBuffer[Node]()
				if (!extensions.isEmpty) children ++= extensions.get
				children += StanzaError(condition, description)
				Stanza.build(tag, PresenceTypeEnumeration.Error.toString, id, to, from, children)
			}
		}
		
		abstract class Presence(xml:Node, val presenceType:PresenceTypeEnumeration.Value) extends Stanza(xml)
		{
			val extensions:Option[Seq[Extension]] = ExtensionsManager.getExtensions(this.xml)
			
			def error(condition:ErrorCondition.Value, description:Option[String]):Node = Error(this, condition, description)
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