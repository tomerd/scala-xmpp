package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object Available
		{
			val presenceType = PresenceTypeEnumeration.Available
			val presenceTypeName = presenceType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Available = apply(id, to, from, None, None, None, None)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]):Available = apply(id, to, from, None, None, None, extension)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], show:Option[Show.Value], status:Option[String], priority:Option[Int], extension:Option[Extension]):Available = apply(build(id, to, from, show, status, priority, extension))
			
			def apply(xml:Node):Available = new Available(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID]):Node = build(id, to, from, None, None, None, None)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]):Node = build(id, to, from, None, None, None, extension)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], show:Option[Show.Value], status:Option[String], priority:Option[Int], extension:Option[Extension]):Node = 
			{
				val children = mutable.ListBuffer[Node]()
				if (!show.isEmpty) children += <show>{ show.get }</show>
				if (!status.isEmpty) children += <status>{ status.get }</status>
				if (!priority.isEmpty) children += <priority>{ priority.get }</priority>			
				Presence.build(presenceType, id, to, from, children, extension)				
			}

		}
		
		class Available(xml:Node) extends Presence(xml, Available.presenceType)
		{
			// getters
			val show:Option[Show.Value] = 
			{
				val show = (this.xml \ "show").text
				if (show.isEmpty) None else Some(Show.withName(show))				
			}
			
			val status:Option[String] = 
			{
				val status = (this.xml \ "status").text
				if (status.isEmpty) None else Some(status)
			}
			
			val priority:Option[Int] = 
			{
				val priority = (this.xml \ "priority").text
				if (priority.isEmpty) None else Some(priority.toInt)
			}
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