package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Headline
		{
			val kind = MessageTypeEnumeration.Headline
			val kindName = kind.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson			
			
			def apply(to:Option[JID], from:Option[JID], body:Option[String]):Headline = apply(None, to, from, None, body, None, None)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], body:Option[String]):Headline = apply(id, to, from, None, body, None, None)
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String]):Headline = apply(id, to, from, subject, body, None, None)
					
			def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extensions:Option[Seq[Extension]]):Headline =
			{
				val xml = Message.build(kind, id, to, from, subject, body, thread, extensions)
				return apply(xml)
			}
			
			def apply(xml:Node):Headline = new Headline(xml)
		}
		
		class Headline(xml:Node) extends Message(xml, Headline.kind)
		{
		}
		
	}
}