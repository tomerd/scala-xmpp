package org.xmpp
{
	package protocol
	{
		import scala.xml._
		
		object StanzaFactory
		{
			// TODO: once XmppComponent removes all dependencies from dom4j need to remove this as well
			// TODO: find a better way to do this			
			def create(element:org.dom4j.Element):Stanza[_] =
			{
				import java.io._
				import org.dom4j.io._
		
				val buffer:StringWriter = new StringWriter()
				val writer:XMLWriter = new XMLWriter(buffer, OutputFormat.createPrettyPrint())
				writer.write(element)
				return create(buffer.toString)
			}
			
			def create(xml:String):Stanza[_] = create(XML.loadString(xml))
						
			def create(xml:Node):Stanza[_] =
			{
				xml.label match
				{
					case "message" => new Message(xml)
					case "presence" => new Presence(xml)
					case "iq" =>
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
		
		abstract class Stanza[T <: Stanza[T]](literal:Node) extends XmlLiteral(literal)
		{			
			def this(other:T) = this(other.xml)
						
			type TypeResolver = { def withName(string:String):Any }
			val typeResolver:TypeResolver
			
			private var _id:Option[String] = None
			private var _to:Option[JID] = None
			private var _from:Option[JID] = None
			private var _kind:Option[Any] = None
			
			final def id:String = 
			{
				_id match
				{
					case None =>
					{
						_id = Some((this.xml \ "@id").text)
						return _id.get
					}
					case Some(id) => id
				}
			}
			
			final def to:JID = 
			{
				_to match
				{
					case None =>
					{
						_to = Some(JID((this.xml \ "@to").text))
						return _to.get
					}
					case Some(jid) => jid
				}
			}
			
			final def from:JID = 
			{
				_from match
				{
					case None =>
					{
						_from = Some(JID((this.xml \ "@from").text))
						return _from.get
					}
					case Some(jid) => jid
				}
			}
			
			// TODO: should not be any
			final def kind:Any = 
			{
				_kind match
				{
					case None =>
					{
						val t = typeResolver.withName((this.xml \ "@type").text)
						_kind = Some(t)
						return _kind.get
					}
					case Some(kind) => kind
				}
			}				
				
			
			// TODO, test this
			final def error:Error = new Error((this.xml \ "@error")(0))

			// FIXME
			final def copy():T = StanzaFactory.create(this.xml).asInstanceOf[T]
		}
		
		object StanzaTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknonw")
			val Error = Value("error")
		}
	}
}