package org.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		object MultiText
		{		
			val fieldType = FieldTypeEnumeration.MultiText
			val fieldTypeName = fieldType.toString
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, children:Option[Seq[Node]]=None):MultiText =
			{
				val xml = Field.build(MultiText.fieldType, identifier, label, description, required, children)
				return apply(xml)
			}
			
			def apply(xml:Node):MultiText = new MultiText(xml)
		}
		
		class MultiText(xml:Node) extends Field(xml, MultiText.fieldType)
		{
		}
		
	}
}
