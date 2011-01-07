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
			def apply():Available =
			{
				val xml = Stanza.build(Presence.TAG)
				return new Available(xml)
			}
			
			def apply(xml:Node):Available = new Available(xml)
		}
		
		class Available(xml:Node) extends Presence(xml)
		{
			// getters
			private var _show:Option[Show.Value] = None
			private def show:Option[Show.Value] = _show
			
			private var _status:Option[String] = None
			private def status:Option[String] = _status
			
			private var _priority:Option[Int] = None
			private def priority:Option[Int] = _priority
			
			def available:Boolean = TypeEnumeration.Available == this.kind
			
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
		}
	}
}