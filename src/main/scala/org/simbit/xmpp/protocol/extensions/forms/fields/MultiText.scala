package org.simbit.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object MultiText
		{		
			val fieldType = FieldTypeEnumeration.MultiText
			val fieldTypeName = fieldType.toString
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, value:Option[String]=None):MultiText =
			{
				val xml = SimpleField.build(MultiText.fieldType, identifier, label, description, required, value)
				return apply(xml)
			}
			
			def apply(xml:Node):MultiText = new MultiText(xml)
		}
		
		class MultiText(xml:Node) extends SimpleField(xml, MultiText.fieldType)
		{
		}
		
	}
}
