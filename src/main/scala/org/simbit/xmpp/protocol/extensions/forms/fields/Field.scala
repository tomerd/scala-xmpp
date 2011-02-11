package org.simbit.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		protected[forms] object Field
		{
			val tag = "field"
			
			def build(fieldType:FieldTypeEnumeration.Value, identifier:Option[String], label:Option[String], description:Option[String], required:Boolean, children:Option[Seq[Node]]):Node =
			{
				val kids = mutable.ListBuffer[Node]()
				if (!description.isEmpty) kids += <desc>{ description }</desc>
				if (required) kids += <required/>
				if (!children.isEmpty) kids ++= children.get
				var metadata:MetaData = new UnprefixedAttribute("type", Text(fieldType.toString), Null)
				if (!identifier.isEmpty) metadata = metadata.append(new UnprefixedAttribute("identifier", Text(identifier.get), Null))
				if (!label.isEmpty) metadata = metadata.append(new UnprefixedAttribute("label", Text(label.get), Null))				
				return Elem(null, tag, metadata, TopScope, kids:_*)
			}
		}
		
		abstract protected[forms] class Field(xml:Node, val fieldType:FieldTypeEnumeration.Value) extends XmlWrapper(xml)
		{
			//val fieldType:FieldTypeEnumeration.Value = FieldTypeEnumeration.withName((this.xml \ "@type").text)
			
			val identifier:Option[String] = (this.xml \ "@identifier").text
			
			val label:Option[String] = (this.xml \ "@label").text
						
			val description:Option[String] = (this.xml \ "description").text
			
			val required:Boolean = Null != (this.xml \ "required")
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
