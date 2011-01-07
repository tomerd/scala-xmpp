package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
					
		object Presence
		{
			val TAG = "presence"

			def apply():Presence = apply(None, None, None, None, None, None, None, None)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[PresenceTypeEnumeration.Value], extensions:Option[Seq[Extension]]):Presence = apply(id, to, from, kind, None, None, None, extensions)
						
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[PresenceTypeEnumeration.Value], show:Option[Show.Value], status:Option[String], priority:Option[Int], extensions:Option[Seq[Extension]]):Presence =
			{
				val children = mutable.ListBuffer[Node]()
				if (!show.isEmpty) children += <show>{ show.get }</show>
				if (!status.isEmpty) children += <status>{ status.get }</status>
				if (!priority.isEmpty) children += <priority>{ priority.get }</priority>
				if (!extensions.isEmpty) children ++= extensions.get
					
				val xml = Stanza.build(TAG, id, to, from, kind, children)
				return apply(xml)
			}
			
			def apply(xml:Node):Presence = PresenceFactory.create(xml).get
			
			/*
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]=None):Presence =
			{
				val xml = Stanza.error(TAG, id, to, from, condition, description)
				return new Presence(xml)
			}
			*/
		}
		
		abstract class Presence(xml:Node) extends Stanza(xml)
		{
			val TypeEnumeration = PresenceTypeEnumeration
			
			def error(condition:ErrorCondition.Value, description:Option[String]=None):Error = Error(this.id, this.from, this.to, condition, description)
		}
		
		object PresenceTypeEnumeration extends Enumeration
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
		
		object Show extends Enumeration
		{
			type Reason = Value
			
			val Unknown = Value("unknown") // internal use
			val Chat = Value("chat")
			val Away = Value("away")
			val XA = Value("xa")
			val DND = Value("dnd")
		}


	}
}