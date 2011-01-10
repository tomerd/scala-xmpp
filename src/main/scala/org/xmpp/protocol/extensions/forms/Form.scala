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

		protected object Form
		{
			/*
			def apply(id:Option[String], to:Option[JID], from:Option[JID], formType:FormTypeEnumeration.Value, fields:Seq[Field]):Form = apply(id, to, from, formType, None, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], formType:FormTypeEnumeration.Value, title:Option[String], fields:Seq[Field]):Form = apply(id, to, from, formType, title, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], formType:FormTypeEnumeration.Value, title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Form = apply(build(id, to, from, formType, title, instructions, fields))
 			
			def apply(xml:Node):Form = new Form(xml)
			*/
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], formType:FormTypeEnumeration.Value, title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
			{
				val children = mutable.ListBuffer[Node]()
				if (!title.isEmpty) children += <title>{ title }</title>
				if (!instructions.isEmpty) instructions.get.foreach( instruction => children += <instructions>{ instruction }</instructions>)
				//if (!reported.isEmpty) children += <reported>{ reported }</reported>
				fields.foreach( field => children += field)
				var metadata:MetaData = new UnprefixedAttribute("type", Text(formType.toString), Null)
				return Container.build(id, to, from, X(FormFactory.namespace, metadata, children))
			}
		}
		
		abstract class Form(xml:Node, val formType:FormTypeEnumeration.Value) extends Container(xml)
		{
			//val formType:FormTypeEnumeration.Value = FormTypeEnumeration.withName((this.xml \ "@type").text)
			
		 	val title:Option[String] = (this.xml \ "title").text
			
			//val reported:Option[String] = (this.xml \ "reported").text
			
			val instructions:Option[Seq[String]] = (this.xml \ "instructions").map( node => node.text )
			
			val fields:Seq[Field] = (this.xml \ "fields").map( node => FieldFactory.create(node) )
		}
		
		protected object FormTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val Form = Value("form")
			val Submit = Value("submit")
			val Result = Value("result")
			val Cancel = Value("cancel")
		}
		
	}
}
