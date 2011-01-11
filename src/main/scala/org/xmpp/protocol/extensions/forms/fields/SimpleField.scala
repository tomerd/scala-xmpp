package org.xmpp
{
	package protocol.extensions.forms.fields
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
		
	}
}
