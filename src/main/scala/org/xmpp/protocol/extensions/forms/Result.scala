package org.xmpp
{
	package protocol.extensions.forms
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.extensions.forms.fields._
		
		import org.xmpp.protocol.Protocol._
		
		object Result 
		{
			val formType = FormTypeEnumeration.Form
			val formTypeName = formType.toString
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], fields:Seq[Field]):Result = apply(id, to, from, None, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], fields:Seq[Field]):Result = apply(id, to, from, title, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Result = apply(build(id, to, from, title, instructions, fields))
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], reported:ResultHeader, items:Seq[ResultItem]):Result =  apply(build(id, to, from, None, None, reported, items))
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], reported:ResultHeader, items:Seq[ResultItem]):Result =  apply(build(id, to, from, title, None, reported, items))
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], reported:ResultHeader, items:Seq[ResultItem]):Result =  apply(build(id, to, from, title, instructions, reported, items))
 			
			def apply(xml:Node):Result = new Result(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
			{
				Form.build(id, to, from, Result.formType, title, instructions, fields)
			}
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], title:Option[String], instructions:Option[Seq[String]], reported:ResultHeader, items:Seq[ResultItem]):Node =
			{
				val children = mutable.ListBuffer[Node]()
				children += reported
				children ++= items
				Form.build(id, to, from, Result.formType, title, instructions, children)
			}			
		}
		
		class Result(xml:Node) extends Form(xml, Result.formType)
		{
		}
		
	}
}
