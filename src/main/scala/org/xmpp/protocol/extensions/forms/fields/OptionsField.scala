package org.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
				
		object OptionsField
		{
			/*
			def apply(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, options:Seq[FieldOption]=Nil):OptionsField = apply(build(fieldType, identifier, label, description, required, options))
			
			def apply(xml:Node):OptionsField = new OptionsField(xml)
			*/
			def build(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, options:Seq[FieldOption]=Nil):Node =
			{
				Field.build(fieldType, identifier, label, description, required, options)
			}
		}
		
		abstract class OptionsField(xml:Node, fieldType:FieldTypeEnumeration.Value) extends Field(xml, fieldType)
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
		
		
	}
}
