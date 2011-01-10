package org.xmpp
{
	package protocol.extensions.forms
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.extensions.forms.fields._
		
		import org.xmpp.protocol.Protocol._
		
		object Submit 
		{
			val formType = FormTypeEnumeration.Form
			val formTypeName = formType.toString
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], fields:Seq[Field]):Submit = apply(id, to, from, None, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], fields:Seq[Field]):Submit = apply(id, to, from, title, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Submit = apply(build(id, to, from, title, instructions, fields))
 			
			def apply(xml:Node):Submit = new Submit(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
			{
				Form.build(id, to, from, Submit.formType, title, instructions, fields)
			}
		}
		
		class Submit(xml:Node) extends Form(xml, Submit.formType)
		{
		}
		
	}
}
