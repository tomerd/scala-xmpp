package org.xmpp
{
	package protocol.extensions.forms
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._		
		import org.xmpp.protocol.extensions.forms.fields._
		
		import org.xmpp.protocol.Protocol._
		
		object Basic
		{
			val formType = FormTypeEnumeration.Form
			val formTypeName = formType.toString
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], fields:Seq[Field]):Basic = apply(id, to, from, None, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], fields:Seq[Field]):Basic = apply(id, to, from, title, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Basic = apply(build(id, to, from, title, instructions, fields))
 			
			def apply(xml:Node):Basic = new Basic(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
			{
				Form.build(id, to, from, Basic.formType, title, instructions, fields)
			}
		}
		
		class Basic(xml:Node) extends Form(xml, Basic.formType)
		{
		}
		
	}
}
