package org.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		object SingleJid
		{		
			val fieldType = FieldTypeEnumeration.SingleJid
			val fieldTypeName = fieldType.toString
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, children:Option[Seq[Node]]=None):SingleJid =
			{
				val xml = Field.build(SingleJid.fieldType, identifier, label, description, required, children)
				return apply(xml)
			}
			
			def apply(xml:Node):SingleJid = new SingleJid(xml)
		}
		
		class SingleJid(xml:Node) extends Field(xml, SingleJid.fieldType)
		{
		}
		
	}
}
