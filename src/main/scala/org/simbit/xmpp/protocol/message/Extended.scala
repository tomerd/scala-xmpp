package org.simbit.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
		
		object Extended
		{
			val messageType = MessageTypeEnumeration.Extended
			val messageTypeName = messageType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(to:JID, from:JID, extensions:Option[Seq[Extension]]):Extended = apply(None, to, from, extensions)
				
			def apply(id:Option[String], to:JID, from:JID, extensions:Option[Seq[Extension]]):Extended =
			{
				val xml = Message.build(messageType, id, to, from, None, None, None, extensions)
				return apply(xml)
			}
			
			def apply(xml:Node):Extended = new Extended(xml)
			
			def unapply(extended:Extended):Option[(Option[String], JID, JID, Option[Seq[Extension]])] = Some(extended.id, extended.to, extended.from, extended.extensions)
		}
		
		class Extended(xml:Node) extends Message(xml, Extended.messageType)
		{
		}
		
	}
}