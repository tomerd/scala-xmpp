package org.simbit.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol.iq._
		import org.simbit.xmpp.protocol.presence._
		import org.simbit.xmpp.protocol.message._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object Stanza
		{			
			def apply(xml:String):Stanza= apply(XML.loadString(xml))
									
			def apply(xml:Node):Stanza = 
			{
				xml.label match
				{
					case IQ.tag => IQFactory.create(xml)
					case Presence.tag => PresenceFactory.create(xml)
					case Message.tag => MessageFactory.create(xml)
					case _ => throw new Exception("unknown stanza type, expected iq, presence or message")
				}				
			}
								
			def build(name:String):Node = build(name, null, None, None, None)
				
			def build(name:String, stanzaType:String, id:Option[String], to:Option[JID], from:Option[JID], children:Option[Seq[Node]]=None):Node = 
			{
				val kids:Seq[Node] = if (!children.isEmpty) children.get else null
				//if (!extension.isEmpty) children += extension.get
				var metadata:MetaData = Null
				if (!id.isEmpty) metadata = metadata.append(new UnprefixedAttribute("id", Text(id.get), Null))
				if (!to.isEmpty) metadata = metadata.append(new UnprefixedAttribute("to", Text(to.get), Null))
				if (!from.isEmpty) metadata = metadata.append(new UnprefixedAttribute("from", Text(from.get), Null))
				if (null != stanzaType && !stanzaType.isEmpty) metadata = metadata.append(new UnprefixedAttribute("type", Text(stanzaType), Null))
				
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
			val id:Option[String] = (this.xml \ "@id").text 
								
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
			
			val extension:Option[Extension] = ExtensionsManager.getExtension(this.xml)
			
			
			/*
			val error:Option[Error] = 
			{
				val errorNodes = (this.xml \ "error")
				if (0 == errorNodes.length) None else Some(new Error(errorNodes(0)))
			}
			*/	
		}
		
	}
}