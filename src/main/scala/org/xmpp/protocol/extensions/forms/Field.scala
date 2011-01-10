package org.xmpp
{
	package protocol.extensions.forms
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		object SimpleField 
		{
			def apply(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, value:Option[String]=None):SimpleField = apply(build(fieldType, identifier, label, description, required, value))
			
			def apply(xml:Node):SimpleField = new SimpleField(xml)
			
			def build(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, value:Option[String]=None):Node =
			{
				val children = mutable.ListBuffer[Node]()
				if (!value.isEmpty) children += <value>{ value }</value>
				Field.build(fieldType, identifier, label, description, required, children)
			}
		}
		
		class SimpleField(xml:Node) extends Field(xml)
		{	
			val value:Option[String] = (this.xml \ "value").text
		}		
				
		object OptionsField
		{
			def apply(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, options:Seq[FieldOption]=Nil):OptionsField = apply(build(fieldType, identifier, label, description, required, options))
			
			def apply(xml:Node):OptionsField = new OptionsField(xml)
			
			def build(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, options:Seq[FieldOption]=Nil):Node =
			{
				Field.build(fieldType, identifier, label, description, required, options)
			}			
		}
		
		class OptionsField(xml:Node) extends Field(xml)
		{
			val options:Seq[FieldOption] = (this.xml \ "option").map( option => FieldOption(option) )
		}		
		
		object FieldOption
		{
			val tag = "option"
			
			def apply(label:String, value:String):FieldOption = apply(build(label, value))
			
			def apply(xml:Node):FieldOption = new FieldOption(xml)
			
			def build(label:String, value:String):Node = Elem(null, tag, new UnprefixedAttribute("label", Text(label.toString), Null), TopScope, <value>{ value }</value>)
		}
		
		class FieldOption(xml:Node) extends XmlWrapper(xml)
		{
			val label:String = (this.xml \ "@label").text
			
			val value:String = (this.xml \ "value").text
		}
		
		protected object FieldFactory
		{
			def create(xml:Node):Field =
			{			
				xml match
				{
					case field @ <field/> if !(field \ "value").isEmpty => SimpleField(xml)
					case field @ <field/> if !(field \ "option").isEmpty => OptionsField(xml)
					case _ => throw new Exception("unknown field")
				}
			}
		}
		
		protected object Field
		{
			val tag = "field"				
			
			def build(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, children:Option[Seq[Node]]=None):Node =
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
		
		abstract protected class Field(xml:Node) extends XmlWrapper(xml)
		{			
			val fieldType:FieldTypeEnumeration.Value = FieldTypeEnumeration.withName((this.xml \ "@type").text)
			
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
