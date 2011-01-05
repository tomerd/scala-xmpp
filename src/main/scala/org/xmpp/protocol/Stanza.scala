package org.xmpp
{
	package protocol
	{
		import scala.xml._
				
		final object Stanza
		{
			// TODO: once XmppComponent removes all dependencies from dom4j need to remove this as well
			// TODO: find a better way to do this
			def apply(element:org.dom4j.Element):Stanza[_] =
			{
				import java.io._
				import org.dom4j.io._
		
				val buffer:StringWriter = new StringWriter()
				val writer:XMLWriter = new XMLWriter(buffer, OutputFormat.createPrettyPrint())
				writer.write(element)
				return Stanza(buffer.toString)
			}
			
			def apply(xml:String):Stanza[_] = Stanza(XML.loadString(xml))
						
			def apply(xml:Node):Stanza[_] = 
			{
				val stanza = xml.label.toLowerCase match
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
		}
		
		abstract class Stanza[T <: Stanza[T]](xml:Node) extends XmlLiteral(xml)
		{				
			def this(other:T) = this(other.xml)
				
			val TypeEnumeration:Enumeration
			
			// getters			
			private var _id:Option[String] = None
			final def id:Option[String] = _id
			
			private var _to:Option[JID] = None
			final def to:Option[JID] = _to
			
			private var _from:Option[JID] = None
			final def from:Option[JID] = _from
			
			private var _kind:Option[TypeEnumeration.Value] = None
			final def kind:Option[TypeEnumeration.Value] = _kind
			
			private var _language:Option[String] = None
			final def language:Option[String] = _language
			
			private var _error:Option[Error] = None
			final def error:Option[Error] = _error
						
			protected def parse
			{
				val id = (this.xml \ "@id").text
				_id = if (id.isEmpty) None else Some(id)

				val to = (this.xml \ "@to").text
				_to = if (to.isEmpty) None else Some(JID(to))

				val from = (this.xml \ "@from").text
				_from = if (from.isEmpty) None else Some(JID(from))
				
				val kind = (this.xml \ "@type").text
				_kind = if (kind.isEmpty) None else Some(TypeEnumeration.withName(kind))

				// TODO: find a better way to query this prefixed attribute with no namespace, this does not work: (this.xml \ "@lang").text
				_language = this.xml.attributes.find((attribute) => "lang" == attribute.key) match
				{
					case Some(attribute) => if (attribute.value.text.isEmpty) None else Some(attribute.value.text)
					case None => None
				}

				val errorNodes = (this.xml \ "error")
				_error = if (0 == errorNodes.length) None else Some(Error(errorNodes(0)))				
			}

			// test this
			final def copy():T = Stanza(this.xml).asInstanceOf[T]		
		}
		
	}
}