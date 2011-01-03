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
			implicit def wrapper2node(wrapper:XmlWrapper):Node = wrapper.xml
			implicit def wrapper2string(wrapper:XmlWrapper):String = wrapper.toString
			
			implicit def string2stanza(xml:String):Stanza[_] = StanzaFactory.create(xml)
			implicit def node2stanza(xml:Node):Stanza[_] = StanzaFactory.create(xml)
			
			implicit def node2String(xml:Node):String = xml.toString
			implicit def string2node(xml:String):Node = XML.loadString(xml)
			
			implicit def jid2string(jid:JID):String = jid.toString
			implicit def string2jid(string:String) = JID(string)
		}
	}
}