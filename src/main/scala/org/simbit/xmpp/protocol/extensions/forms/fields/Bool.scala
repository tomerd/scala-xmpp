package org.simbit.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object Bool
		{		
			val fieldType = FieldTypeEnumeration.Bool
			val fieldTypeName = fieldType.toString
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, value:Option[Boolean]=None):Bool =
			{
				val xml = SimpleField.build(Bool.fieldType, identifier, label, description, required, value)
				return apply(xml)
			}
			
			def apply(xml:Node):Bool = new Bool(xml)
		}
		
		class Bool(xml:Node) extends SimpleField(xml, Bool.fieldType)
		{
		}
		
	}
}
