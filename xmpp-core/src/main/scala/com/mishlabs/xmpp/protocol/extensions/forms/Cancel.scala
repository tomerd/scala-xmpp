package com.mishlabs.xmpp
{
	package protocol.extensions.forms
	{
		import scala.collection._
		import scala.xml._
		
		import com.mishlabs.xmpp.protocol._
		import com.mishlabs.xmpp.protocol.extensions.forms.fields._
		
		import com.mishlabs.xmpp.protocol.Protocol._
		
		object Cancel 
		{
			val formType = FormTypeEnumeration.Form
			val formTypeName = formType.toString
			
			def apply(fields:Seq[Field]):Cancel = apply(None, None, fields)
			
			def apply(title:Option[String], fields:Seq[Field]):Cancel = apply(title, None, fields)
			
			def apply(title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Cancel = apply(build(title, instructions, fields))
 			
			def apply(xml:Node):Cancel = new Cancel(xml)
			
			def build(title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
			{
				Form.build(Cancel.formType, title, instructions, fields)
			}
		}
		
		class Cancel(xml:Node) extends Form(xml, Cancel.formType)
		{
		}
		
	}
}
