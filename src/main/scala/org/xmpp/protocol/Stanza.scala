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
			/*
			{
				val stanza:Stanza = xml.label.toLowerCase match
				{
					case Message.TAG => new Message(xml)
					case Presence.TAG => new Presence(xml)
					case IQ.TAG =>
					{
						// FIXME
						// TODO: test this
						(xml \ "query")(0).namespace match
						{
							//case "jabber:iq:roster" => new Roster(xml)
							case _ => new IQ(xml)
			            }
					}
					case _ => throw new Exception("unknown stanza: " + xml.label) 
				}	
				
				stanza.parse
				return stanza			
			}
			*/
								
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
			//val TypeEnumeration:Enumeration
			
			// getters
			/*
			private var _id:Option[String] = None
			def id:Option[String] = _id
			
			private var _to:Option[JID] = None
			def to:Option[JID] = _to
			
			private var _from:Option[JID] = None
			def from:Option[JID] = _from
			
			//private var _kind:Option[TypeEnumeration.Value] = None
			//def kind:Option[TypeEnumeration.Value] = _kind
			
			private var _language:Option[String] = None
			def language:Option[String] = _language
			
			private var _error:Option[Error] = None
			def error:Option[Error] = _error
						
			override protected def parse
			{
				super.parse
				
				val id = (this.xml \ "@id").text
				_id = if (id.isEmpty) None else Some(id)

				val to = (this.xml \ "@to").text
				_to = if (to.isEmpty) None else Some(JID(to))

				val from = (this.xml \ "@from").text
				_from = if (from.isEmpty) None else Some(JID(from))
				
				//val kind = (this.xml \ "@type").text
				//_kind = if (kind.isEmpty) None else Some(TypeEnumeration.withName(kind))

				// TODO: find a better way to query this prefixed attribute with no namespace, this does not work: (this.xml \ "@lang").text
				_language = this.xml.attributes.find((attribute) => "lang" == attribute.key) match
				{
					case Some(attribute) => if (attribute.value.text.isEmpty) None else Some(attribute.value.text)
					case None => None
				}

				val errorNodes = (this.xml \ "error")
				_error = if (0 == errorNodes.length) None else Some(new Error(errorNodes(0)))				
			}
			*/
			
			def id:Option[String] = 
			{
				val id = (this.xml \ "@id").text
				if (id.isEmpty) None else Some(id)
			}
					
			def to:Option[JID] = 
			{
				val to = (this.xml \ "@to").text
				if (to.isEmpty) None else Some(JID(to))
			}
			
			def from:Option[JID] = 
			{
				val from = (this.xml \ "@from").text
				if (from.isEmpty) None else Some(JID(from))
			}
			
			def language:Option[String] = 
			{
				// TODO: find a better way to query this prefixed attribute with no namespace, this does not work: (this.xml \ "@lang").text
				this.xml.attributes.find((attribute) => "lang" == attribute.key) match
				{
					case Some(attribute) => if (attribute.value.text.isEmpty) None else Some(attribute.value.text)
					case None => None
				}
			}
			
			def error:Option[Error] = 
			{
				val errorNodes = (this.xml \ "error")
				if (0 == errorNodes.length) None else Some(new Error(errorNodes(0)))
			}
			
			def getExtensionByName(name:String):Option[Extension] =
			{
				this.xml.child.find( child => name == child.label ) match
				{
					case None => None
					case Some(node) => Some(new Extension(node))
				}
			}
			
			def getExtensionByNamespace(name:String):Option[Extension] =
			{
				this.xml.child.find( child => name == child.namespace ) match
				{
					case None => None
					case Some(node) => Some(new Extension(node))
				}
			}
	
		}
		
	}
}