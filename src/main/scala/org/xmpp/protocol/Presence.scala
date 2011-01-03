package org.xmpp
{
	package protocol
	{
		import scala.xml._
		
		class Presence(literal:Node) extends Stanza[Presence](literal)
		{
			val TypeEnumeration = PresenceTypeEnumeration
			
			final def available:Boolean = PresenceTypeEnumeration.Available == this.kind
			
			final  def show:Show.Value = Show.withName((this.xml \ "show").text) 
			
			final def status:String = (this.xml \ "status").text
			
			final def priority:Int = (this.xml \ "priority").text.toInt
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