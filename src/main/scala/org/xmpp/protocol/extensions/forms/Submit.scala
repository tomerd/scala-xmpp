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
			
			def apply(fields:Seq[Field]):Submit = apply(None, None, fields)
			
			def apply(title:Option[String], fields:Seq[Field]):Submit = apply(title, None, fields)
			
			def apply(title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Submit = apply(build(title, instructions, fields))
 			
			def apply(xml:Node):Submit = new Submit(xml)
			
			def build(title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
			{
				Form.build(Submit.formType, title, instructions, fields)
			}
		}
		
		class Submit(xml:Node) extends Form(xml, Submit.formType)
		{
		}
		
	}
}
