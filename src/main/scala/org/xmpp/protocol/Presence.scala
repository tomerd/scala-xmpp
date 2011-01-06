package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._
		
		final object Presence
		{
			val TAG = "presence"
				
			def apply():Presence = apply(None, None, None, None, None, None, None, None)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[PresenceTypeEnumeration.Value], show:Option[Show.Value], status:Option[String], priority:Option[Int], extensions:Option[Seq[Extension]]):Presence =
			{
				val children = mutable.ListBuffer[Node]()
				if (!show.isEmpty) children += <show>{ show.get }</show>
				if (!status.isEmpty) children += <status>{ status.get }</status>
				if (!priority.isEmpty) children += <priority>{ priority.get }</priority>
				if (!extensions.isEmpty) children ++= extensions.get
					
				val xml = Stanza.build(TAG, id, to, from, kind, children)
				return new Presence(xml)
			}
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]=None):Presence =
			{
				val xml = Stanza.error(TAG, id, to, from, condition, description)
				return new Presence(xml)
			}
		}
		
		class Presence(xml:Node) extends Stanza(xml)
		{
			val TypeEnumeration = PresenceTypeEnumeration
			
			// getters
			private var _show:Option[Show.Value] = None
			private def show:Option[Show.Value] = _show
			
			private var _status:Option[String] = None
			private def status:Option[String] = _status
			
			private var _priority:Option[Int] = None
			private def priority:Option[Int] = _priority
			
			final def available:Boolean = PresenceTypeEnumeration.Available == this.kind
			
			override protected def parse
			{
				super.parse
				
				val show = (this.xml \ "show").text
				_show = if (show.isEmpty) None else Some(Show.withName(show))
				
				val status = (this.xml \ "status").text
				_status = if (status.isEmpty) None else Some(status)
				
				val priority = (this.xml \ "priority").text
				_priority = if (priority.isEmpty) None else Some(priority.toInt)				
			}
			
			final def error(condition:ErrorCondition.Value, description:Option[String]=None):Presence = Presence.error(this.id, this.from, this.to, condition, description)
		}
		
		final object PresenceTypeEnumeration extends Enumeration
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
		
		final object Show extends Enumeration
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