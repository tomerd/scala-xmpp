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
			val kind = PresenceTypeEnumeration.Available
			val kindName = kind.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):Available = apply(id, to, from, None, None, None, None)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Available = apply(id, to, from, None, None, None, extensions)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], show:Option[Show.Value], status:Option[String], priority:Option[Int], extensions:Option[Seq[Extension]]):Available =
			{					
				val xml = Presence.build(kind, id, to, from, show, status, priority, extensions)
				return apply(xml)
			}
			
			def apply(xml:Node):Available = new Available(xml)
		}
		
		class Available(xml:Node) extends Presence(xml, Available.kind)
		{
			// getters
			/*
			private var _show:Option[Show.Value] = None
			private def show:Option[Show.Value] = _show
			
			private var _status:Option[String] = None
			private def status:Option[String] = _status
			
			private var _priority:Option[Int] = None
			private def priority:Option[Int] = _priority
						
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
			*/
			
			private def show:Option[Show.Value] = 
			{
				val show = (this.xml \ "show").text
				if (show.isEmpty) None else Some(Show.withName(show))				
			}
			
			private def status:Option[String] = 
			{
				val status = (this.xml \ "status").text
				if (status.isEmpty) None else Some(status)
			}
			
			private def priority:Option[Int] = 
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