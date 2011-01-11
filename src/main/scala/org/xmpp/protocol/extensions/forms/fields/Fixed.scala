package org.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		object Fixed
		{		
			val fieldType = FieldTypeEnumeration.Fixed
			val fieldTypeName = fieldType.toString
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, children:Option[Seq[Node]]=None):Fixed =
			{
				val xml = Field.build(Fixed.fieldType, identifier, label, description, required, children)
				return apply(xml)
			}
			
			def apply(xml:Node):Fixed = new Fixed(xml)
		}
		
		class Fixed(xml:Node) extends Field(xml, Fixed.fieldType)
		{
		}
		
	}
}
