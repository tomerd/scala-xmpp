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
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, children:Option[Seq[Node]]=None):SingleList =
			{
				val xml = Field.build(SingleList.fieldType, identifier, label, description, required, children)
				return apply(xml)
			}
			
			def apply(xml:Node):SingleList = new SingleList(xml)
		}
		
		class SingleList(xml:Node) extends Field(xml, SingleList.fieldType)
		{
		}
		
	}
}
