package org.xmpp
{
	package protocol
	{
		import scala.xml._

		
		abstract class XmlWrapper
		{
			def xml:Node
			
			final override def toString = xml.toString			
		}
		
		abstract class XmlLiteral(literal:Node) extends XmlWrapper
		{
			def this(xml:String) = this(XML.loadString(xml))
			
			final def xml:Node = literal
		}
	
		object Protocol
		{			
			implicit def string2box(string:String):Option[String] = if ((null != string) && (!string.isEmpty)) Some(string) else None
			implicit def jid2box(jid:JID):Option[JID] = if (null != jid) Some(jid) else None
			
			implicit def wrapper2node(wrapper:XmlWrapper):Node = wrapper.xml
			implicit def wrapper2string(wrapper:XmlWrapper):String = wrapper.toString
			
			implicit def string2stanza(xml:String):Stanza[_] = Stanza(xml)
			implicit def node2stanza(xml:Node):Stanza[_] = Stanza(xml)
			
			implicit def node2String(xml:Node):String = xml.toString
			implicit def string2node(xml:String):Node = XML.loadString(xml)
			
			implicit def jid2string(jid:JID):String = jid.toString
			implicit def string2jid(string:String) = JID(string)
		}
	}
}