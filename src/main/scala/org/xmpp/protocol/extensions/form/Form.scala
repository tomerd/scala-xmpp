package org.xmpp
{
	package protocol.extensions.form
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.message._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object Form extends ExtendedStanzaBuilder[Form]
		{
			val stanzaType = Container.stanzaTypeName
			val name = X.name
			val namespace = "jabber:x:data"
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], formType:FormTypeEnumeration.Value, fields:Option[Seq[Field]]):Form = apply(id, to, from, formType, None, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], formType:FormTypeEnumeration.Value, title:Option[String], fields:Option[Seq[Field]]):Form = apply(id, to, from, formType, title, None, fields)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], formType:FormTypeEnumeration.Value, title:Option[String], instructions:Option[String], fields:Option[Seq[Field]]):Form = apply(build(id, to, from, formType, title, instructions, fields))
 			
			def apply(xml:Node):Form = new Form(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], formType:FormTypeEnumeration.Value, title:Option[String], instructions:Option[String], fields:Option[Seq[Field]]):Node =
			{
				val children = mutable.ListBuffer[Node]()
				if (!fields.isEmpty) fields.get.foreach( field => children += field)
				var metadata:MetaData = new UnprefixedAttribute("type", Text(formType.toString), Null)
				if (!title.isEmpty) metadata = metadata.append(new UnprefixedAttribute("title", Text(title.get), Null))
				if (!instructions.isEmpty) metadata = metadata.append(new UnprefixedAttribute("instructions", Text(instructions.get.toString), Null))
				
				val xml = Container.build(id, to, from, X(namespace, metadata, children))
				return apply(xml)
			}
		}
		
		class Form(xml:Node) extends Container(xml)
		{
			val formType:FieldTypeEnumeration.Value = FieldTypeEnumeration.withName((this.xml \ "@type").text)
			
		 	val title:Option[String] = (this.xml \ "@title").text
			
			val instructions:Option[String] = (this.xml \ "@instructions").text
		}
		
		object FormTypeEnumeration extends Enumeration
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
