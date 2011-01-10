package org.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
			
		object Available
		{
			val stanzaType = PresenceTypeEnumeration.Available
			val stanzaTypeName = stanzaType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Available = apply(id, to, from, None, None, None, None)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]):Available = apply(id, to, from, None, None, None, extension)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], show:Option[Show.Value], status:Option[String], priority:Option[Int], extension:Option[Extension]):Available =
			{					
				val xml = Presence.build(stanzaType, id, to, from, show, status, priority, extension)
				return apply(xml)
			}
			
			def apply(xml:Node):Available = new Available(xml)
		}
		
		class Available(xml:Node) extends Presence(xml, Available.stanzaType)
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