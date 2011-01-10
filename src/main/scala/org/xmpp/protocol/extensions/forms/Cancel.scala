package org.xmpp
{
	package protocol.extensions.forms
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.message._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object Cancel 
		{
			val formType = FormTypeEnumeration.Form
			val formTypeName = formType.toString
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], fields:Seq[Field]):Cancel = apply(id, to, from, None, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], fields:Seq[Field]):Cancel = apply(id, to, from, title, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Cancel = apply(build(id, to, from, title, instructions, fields))
 			
			def apply(xml:Node):Cancel = new Cancel(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
			{
				Form.build(id, to, from, Cancel.formType, title, instructions, fields)
			}
		}
		
		class Cancel(xml:Node) extends Form(xml, Cancel.formType)
		{
		}
		
	}
}
