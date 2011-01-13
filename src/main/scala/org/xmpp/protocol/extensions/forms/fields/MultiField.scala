package org.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		protected object MultiField 
		{			
			def build(fieldType:FieldTypeEnumeration.Value, identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, values:Option[Seq[String]]=None):Node =
			{
				val children = mutable.ListBuffer[Node]()
				if (!values.isEmpty) values.get.foreach( value => children += <value>{ value }</value> )
				Field.build(fieldType, identifier, label, description, required, children)
			}
		}
		
		abstract class MultiField(xml:Node, fieldType:FieldTypeEnumeration.Value) extends Field(xml, fieldType)
		{
			val value:Option[String] = (this.xml \ "value").text
		}
		
	}
}
