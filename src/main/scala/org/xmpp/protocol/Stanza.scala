package org.xmpp
{
	package protocol
	{
		import scala.xml._
				
		final object Stanza
		{
			// TODO: once XmppComponent removes all dependencies from dom4j need to remove this as well
			// TODO: find a better way to do this			
			//def create(element:org.dom4j.Element):Stanza[_] =
			def apply(element:org.dom4j.Element):Stanza[_] =
			{
				import java.io._
				import org.dom4j.io._
		
				val buffer:StringWriter = new StringWriter()
				val writer:XMLWriter = new XMLWriter(buffer, OutputFormat.createPrettyPrint())
				writer.write(element)
				return Stanza(buffer.toString)
			}
			
			//def create(xml:String):Stanza[_] = create(XML.loadString(xml))
			def apply(xml:String):Stanza[_] = Stanza(XML.loadString(xml))
						
			//def create(xml:Node):Stanza[_] =
			def apply(xml:Node):Stanza[_] = 
			{
				xml.label.toLowerCase match
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
			}
		}
		
		abstract class Stanza[T <: Stanza[T]](xml:Node) extends XmlLiteral(xml)
		{			
			def this(other:T) = this(other.xml)
					
			val TypeEnumeration:Enumeration
			
			private var _id:Option[String] = None
			private var _to:Option[JID] = None
			private var _from:Option[JID] = None
			private var _kind:Option[TypeEnumeration.Value] = None
			private var _language:Option[String] = None
			private var _error:Option[Error] = None
			
			final def id:String = 
			{
				_id match
				{
					case Some(id) => id	
					case None => _id = Some((this.xml \ "@id").text); _id.get
				}
			}
			
			final def to:JID = 
			{
				_to match
				{
					case Some(jid) => jid	
					case None => _to = Some(JID((this.xml \ "@to").text)); _to.get
				}
			}
			
			final def from:JID = 
			{
				_from match
				{
					case Some(jid) => jid
					case None => _from = Some(JID((this.xml \ "@from").text)); _from.get
				}
			}
			
			final def kind:TypeEnumeration.Value = 
			{
				_kind match
				{
					case Some(kind) => kind
					case None => _kind = Some(TypeEnumeration.withName((this.xml \ "@type").text)); _kind.get
				}
			}		
			
			final def language:String =
			{
				_language match
				{
					case Some(language) => language
					case None =>
					{		
						// TODO: find a better way to query this prefixed attribute with no namespace, this does not work: (this.xml \ "@lang").text
						val t = this.xml.attributes.find((attribute) => "lang" == attribute.key)
						_language = Some(t.get.value.text)
						return _language.get
					}
				}								
			}
							
			final def error:Option[Error] = 
			{
				_error match
				{
					case Some(error) => Some(error)
					case None =>
					{
						(this.xml \ "error").length match
						{
							case 0 => None
							case _ => _error = Some(Error((this.xml \ "@error")(0))); _error
						}			
					}
				}
			}

			// test this
			final def copy():T = Stanza(this.xml).asInstanceOf[T]
		}
		
	}
}