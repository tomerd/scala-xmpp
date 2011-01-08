package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
				
		object Normal
		{
			def apply(to:Option[JID], from:Option[JID], body:Option[String]):Normal = apply(None, to, from, None, body, None, None)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], body:Option[String]):Normal = apply(id, to, from, None, body, None, None)
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):Normal = apply(id, to, from, subject, body, None, None)
					
			def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):Normal =
			{
				val xml = Message.build(MessageTypeEnumeration.Normal, id, to, from, subject, body, thread, extensions)
				return apply(xml)
			}
			
			def apply(xml:Node):Normal = new Normal(xml)
		}
		
		class Normal(xml:Node) extends Message(xml, MessageTypeEnumeration.Normal)
		{
		}
		
	}
}