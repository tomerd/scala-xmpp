package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._
		
		object Stanza
		{
			// TODO: once XmppComponent removes all dependencies from dom4j need to remove this as well
			// TODO: find a better way to do this
			def apply(element:org.dom4j.Element):Stanza =
			{
				import java.io._
				import org.dom4j.io._
		
				val buffer:StringWriter = new StringWriter()
				val writer:XMLWriter = new XMLWriter(buffer, OutputFormat.createPrettyPrint())
				writer.write(element)
				return apply(buffer.toString)
			}
			
			def apply(xml:String):Stanza= apply(XML.loadString(xml))
									
			def apply(xml:Node):Stanza = StanzaFactory.create(xml)
								
			def build(name:String):Node = build(name, null, None, None, None)
				
			// TODO, find a better solution for the type attribute than Any
			def build(name:String, kind:String, id:Option[String], to:Option[JID], from:Option[JID], children:Option[Seq[Node]]=None):Node = 
			{
				val kids:Seq[Node] = if (!children.isEmpty) children.get else null
				//if (!extension.isEmpty) children += extension.get
				var metadata:MetaData = Null
				if (!id.isEmpty) metadata = metadata.append(new UnprefixedAttribute("id", Text(id.get), Null))
				if (!to.isEmpty) metadata = metadata.append(new UnprefixedAttribute("to", Text(to.get), Null))
				if (!from.isEmpty) metadata = metadata.append(new UnprefixedAttribute("from", Text(from.get), Null))
				if (null != kind && !kind.isEmpty) metadata = metadata.append(new UnprefixedAttribute("type", Text(kind), Null))
				
				return Elem(null, name, metadata, TopScope, kids:_*)
			}
			
			def error(name:String, id:Option[String], to:Option[JID], from:Option[JID], errorCondition:ErrorCondition.Value, errorDescription:Option[String]=None):Node =
			{
				var metadata:MetaData = new UnprefixedAttribute("type", Text("error"), Null)
				if (!id.isEmpty) metadata = metadata.append(new UnprefixedAttribute("id", Text(id.get), Null))
				if (!to.isEmpty) metadata = metadata.append(new UnprefixedAttribute("to", Text(to.get), Null))
				if (!from.isEmpty) metadata = metadata.append(new UnprefixedAttribute("from", Text(from.get), Null))		
				return Elem(null, name, metadata, TopScope, Error(errorCondition, errorDescription))				
			}			
		}
		
		abstract class Stanza(xml:Node) extends XmlWrapper(xml)
		{
			val id:Option[String] = 
			{
				val id = (this.xml \ "@id").text
				if (id.isEmpty) None else Some(id)
			}
					
			val to:Option[JID] = 
			{
				val to = (this.xml \ "@to").text
				if (to.isEmpty) None else Some(JID(to))
			}
			
			val from:Option[JID] = 
			{
				val from = (this.xml \ "@from").text
				if (from.isEmpty) None else Some(JID(from))
			}
			
			val language:Option[String] = 
			{
				// TODO: find a better way to query this prefixed attribute with no namespace, this does not work: (this.xml \ "@lang").text
				this.xml.attributes.find((attribute) => "lang" == attribute.key) match
				{
					case Some(attribute) => if (attribute.value.text.isEmpty) None else Some(attribute.value.text)
					case None => None
				}
			}
			
			val error:Option[Error] = 
			{
				val errorNodes = (this.xml \ "error")
				if (0 == errorNodes.length) None else Some(new Error(errorNodes(0)))
			}
	
		}
		
	}
}