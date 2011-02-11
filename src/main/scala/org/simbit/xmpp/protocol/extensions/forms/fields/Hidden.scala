package org.simbit.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object Hidden
		{		
			val fieldType = FieldTypeEnumeration.Hidden
			val fieldTypeName = fieldType.toString
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, value:Option[String]=None):Hidden =
			{
				val xml = SimpleField.build(Hidden.fieldType, identifier, label, description, required, value)
				return apply(xml)
			}
			
			def apply(xml:Node):Hidden = new Hidden(xml)
		}
		
		class Hidden(xml:Node) extends SimpleField(xml, Hidden.fieldType)
		{
		}
		
	}
}
