package org.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		object SingleList
		{		
			val fieldType = FieldTypeEnumeration.SingleList
			val fieldTypeName = fieldType.toString
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, options:Option[Seq[FieldOption]]=None):SingleList =
			{
				val xml = OptionsField.build(SingleList.fieldType, identifier, label, description, required, options)
				return apply(xml)
			}
			
			def apply(xml:Node):SingleList = new SingleList(xml)
		}
		
		class SingleList(xml:Node) extends OptionsField(xml, SingleList.fieldType)
		{
		}
		
	}
}
