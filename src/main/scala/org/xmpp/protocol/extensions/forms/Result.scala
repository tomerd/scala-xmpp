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
			
			def apply(fields:Seq[Field]):Result = apply(None, None, fields)
			
			def apply(title:Option[String], fields:Seq[Field]):Result = apply(title, None, fields)
			
			def apply(title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Result = apply(build(title, instructions, fields))
			
			def apply(reported:ResultHeader, items:Seq[ResultItem]):Result =  apply(build(None, None, reported, items))
			
			def apply(title:Option[String], reported:ResultHeader, items:Seq[ResultItem]):Result =  apply(build(title, None, reported, items))
			
			def apply(title:Option[String], instructions:Option[Seq[String]], reported:ResultHeader, items:Seq[ResultItem]):Result =  apply(build(title, instructions, reported, items))
 			
			def apply(xml:Node):Result = new Result(xml)
			
			def build(title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
			{
				Form.build(Result.formType, title, instructions, fields)
			}
			
			def build(title:Option[String], instructions:Option[Seq[String]], reported:ResultHeader, items:Seq[ResultItem]):Node =
			{
				val children = mutable.ListBuffer[Node]()
				children += reported
				children ++= items
				Form.build(Result.formType, title, instructions, children)
			}			
		}
		
		class Result(xml:Node) extends Form(xml, Result.formType)
		{
		}
		
	}
}
