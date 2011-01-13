package org.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		object MultiJid
		{		
			val fieldType = FieldTypeEnumeration.MultiJid
			val fieldTypeName = fieldType.toString
			
			def apply(identifier:Option[String]=None, label:Option[String]=None, description:Option[String]=None, required:Boolean=false, jids:Option[Seq[JID]]=None):MultiJid =
			{
				//val options = if (!jids.isEmpty) Some(jids.get.map( jid => FieldOption(jid)) ) else None
				val xml = MultiField.build(MultiJid.fieldType, identifier, label, description, required, jids)
				return apply(xml)
			}
			
			def apply(xml:Node):MultiJid = new MultiJid(xml)
		}
		
		class MultiJid(xml:Node) extends MultiField(xml, MultiJid.fieldType)
		{
		}
		
	}
}
