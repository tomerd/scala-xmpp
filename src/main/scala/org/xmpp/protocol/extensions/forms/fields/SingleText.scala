package org.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		object SingleText
		{		
			val fieldType = FieldTypeEnumeration.SingleText
			val fieldTypeName = fieldType.toString
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, children:Option[Seq[Node]]=None):SingleText =
			{
				val xml = Field.build(SingleText.fieldType, identifier, label, description, required, children)
				return apply(xml)
			}
			
			def apply(xml:Node):SingleText = new SingleText(xml)
		}
		
		class SingleText(xml:Node) extends Field(xml, SingleText.fieldType)
		{
		}
		
	}
}
