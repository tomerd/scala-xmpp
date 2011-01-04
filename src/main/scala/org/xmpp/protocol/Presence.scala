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
				
			def apply(id:String, to:JID, from:JID, kind:PresenceTypeEnumeration.Value, show:Option[Show.Value], status:Option[String], priority:Option[Int]):Presence =
			{
				val children = mutable.ListBuffer[Node]()
				if (!show.isEmpty) children += <show>{ show.get }</show>
				if (!status.isEmpty) children += <status>{ status.get }</status>
				if (!priority.isEmpty) children += <priority>{ priority.get }</priority>
				var attributes:MetaData = new UnprefixedAttribute("id", Text(id), Null)
				attributes = attributes.append(new UnprefixedAttribute("to", Text(to), Null))
				attributes = attributes.append(new UnprefixedAttribute("from", Text(from), Null))
				attributes = attributes.append(new UnprefixedAttribute("type", Text(kind.toString), Null))				
				return new Presence(Elem(null, TAG, attributes, TopScope, children:_*))
			}
			
			def error(id:String, to:JID, from:JID, errorCondition:ErrorCondition.Value):Presence = error(id, to, from, errorCondition, None)
			
			def error(id:String, to:JID, from:JID, errorCondition:ErrorCondition.Value, errorDescription:Option[String]):Presence =
			{
				// TODO: test this
				var attributes:MetaData = new UnprefixedAttribute("id", Text(id), Null)
				attributes = attributes.append(new UnprefixedAttribute("to", Text(to), Null))
				attributes = attributes.append(new UnprefixedAttribute("from", Text(from), Null))
				attributes = attributes.append(new UnprefixedAttribute("type", Text(MessageTypeEnumeration.Error.toString), Null))				
				return new Presence(Elem(null, TAG, attributes, TopScope, Error(errorCondition, errorDescription)))				
			}			
		}
		
		class Presence(xml:Node) extends Stanza[Presence](xml)
		{
			val TypeEnumeration = PresenceTypeEnumeration
			
			private var _show:Option[Show.Value] = None
			private var _status:Option[String] = None
			private var _priority:Option[Int] = None
			
			final def available:Boolean = PresenceTypeEnumeration.Available == this.kind
			
			final  def show:Show.Value = 
			{
				_show match
				{
					case Some(show) => show
					case None => _show = Some(Show.withName((this.xml \ "show").text)); _show.get
				}
			}
			
			final def status:String = 
			{
				_status match
				{
					case Some(status) => status
					case None => _status = Some((this.xml \ "status").text); _status.get
				}				
			}
			
			final def priority:Int = 
			{
				_priority match 
				{
					case Some(priority) => priority
					case None => _priority = Some((this.xml \ "priority").text.toInt); _priority.get
				}
			}
			
			final def error(condition:ErrorCondition.Value, description:Option[String]=None) = Message.error(this.id, this.from, this.to, condition, description)
		}
		
		final object PresenceTypeEnumeration extends Enumeration
		{
			type value = Value
			
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
			
			val Unknown = Value("unknown")
			val Chat = Value("chat")
			val Away = Value("away")
			val XA = Value("xa")
			val DND = Value("dnd")
		}
	}
}