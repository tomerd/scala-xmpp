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
			def apply(xml:String):Stanza = apply(XML.loadString(xml))
			
			def apply(xml:Node):Stanza = 
			{
				xml.label match
				{
					case IQ.tag => iq.Builder(xml)
					case Presence.tag => presence.Builder(xml)
					case Message.tag => message.Builder(xml)
					case _ => throw new Exception("unknown stanza type, expected iq, presence or message")
				}
			}
			
			def build(name:String, stanzaType:String, id:Option[String], to:JID, from:JID, children:Option[Seq[Node]]=None):Node = 
			{
				val kids:Seq[Node] = if (!children.isEmpty) children.get else Nil
				//if (!extension.isEmpty) children += extension.get
				var metadata:MetaData = Null
				if (null != stanzaType && !stanzaType.isEmpty) metadata = metadata.append(new UnprefixedAttribute("type", Text(stanzaType), Null))
				if (!id.isEmpty) metadata = metadata.append(new UnprefixedAttribute("id", Text(id.get), Null))
				metadata = metadata.append(new UnprefixedAttribute("to", Text(to), Null))
				metadata = metadata.append(new UnprefixedAttribute("from", Text(from), Null))
				
				return Elem(null, name, metadata, TopScope, kids:_*)
			}
		}
		
		abstract class Stanza(xml:Node) extends XmlWrapper(xml) with Packet
		{
			val id:Option[String] = (this.xml \ "@id").text 
			
			val to:JID = JID((this.xml \ "@to").text)
			
			val from:JID = JID((this.xml \ "@from").text)
			
			val language:Option[String] = 
			{
				// TODO: find a better way to query this prefixed attribute with no namespace, this does not work: (this.xml \ "@lang").text
				this.xml.attributes.find((attribute) => "lang" == attribute.key) match
				{
					case Some(attribute) => if (attribute.value.text.isEmpty) None else Some(attribute.value.text)
					case None => None
				}
			}	
		}
		
	}
}