package org.xmpp
{
	package protocol.message
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Container
		{	
			val stanzaType = MessageTypeEnumeration.None
			val stanzaTypeName = stanzaType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]):Container = apply(id, to, from, None, None, None, extension)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], body:Option[String], extension:Option[Extension]):Container = apply(id, to, from, None, body, None, extension)
									
			def apply(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extension:Option[Extension]):Container = apply(build(id, to, from, subject, body, thread, extension))
						
			def apply(xml:Node):Container = new Container(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]):Node = build(id, to, from, None, None, None, extension)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], body:Option[String], extension:Option[Extension]):Node = build(id, to, from, None, body, None, extension)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], subject:Option[String], body:Option[String], thread:Option[String], extension:Option[Extension]):Node = Message.build(stanzaType, id, to, from, subject, body, thread, extension)
		}
		
		class Container(xml:Node) extends Message(xml, Container.stanzaType)
		{
		}

	}
}