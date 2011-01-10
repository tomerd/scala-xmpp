package org.xmpp
{
	package protocol.extensions.form
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		object Field
		{
			val tag = "field"
			
			def apply(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, value:Option[String]=None):Field = apply(build(fieldType, identifier, label, value))
			
			def apply(xml:Node):Field = new Field(xml)
			
			def build(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, value:Option[String]=None):Node =
			{
				val children = mutable.ListBuffer[Node]()
				if (!value.isEmpty) children += <value>{ value }</value>
				var metadata:MetaData = new UnprefixedAttribute("type", Text(fieldType.toString), Null)
				if (!identifier.isEmpty) metadata = metadata.append(new UnprefixedAttribute("identifier", Text(identifier.get), Null))
				if (!label.isEmpty) metadata = metadata.append(new UnprefixedAttribute("label", Text(label.get), Null))
				return Elem(null, tag, metadata, TopScope, children:_*)
			}
		}
		
		class Field(xml:Node) extends XmlWrapper(xml)
		{			
			val fieldType:FieldTypeEnumeration.Value = FieldTypeEnumeration.withName((this.xml \ "@type").text)
			
			val identifier:Option[String] = (this.xml \ "@identifier").text
			
			val label:Option[String] = (this.xml \ "@label").text
			
			val value:Option[String] = (this.xml \ "value").text
		}
			
		object FieldTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val Bool = Value("boolean")
			val Fixed = Value("fixed")
			val Hidden = Value("hidden")
			val MultiJid = Value("jid-multi")
			val SingleJid = Value("jid-single")
			val MultiList = Value("list-multi")
			val SingleList = Value("list-single")
			val MultiText = Value("text-multi")
			val SingleText = Value("text-single")
			val PrivateText = Value("text-private")
		}
	}
}
