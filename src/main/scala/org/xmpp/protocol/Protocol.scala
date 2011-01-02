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
		
		// TODO: should convert all the packet library to native scala
		//import org.xmpp.packet._
	
		object Protocol
		{
			/*
			// TODO: should convert all the packet library to native scala
			implicit def packet2Stanza(packet:Packet):Stanza = new SimpleStanza(packet.toString)
			implicit def packet2String(packet:Packet):String = packet.toString
			
			implicit def string2Stanza(xml:String):Stanza = new SimpleStanza(xml)
			implicit def node2Stanza(xml:Node):Stanza = new SimpleStanza(xml)
						
			implicit def stanza2String(stanza:Stanza):String = stanza.toString
			implicit def stanza2Node(stanza:Stanza):Node = stanza.toXml
			*/
			
			implicit def wrapper2node(wrapper:XmlWrapper):Node = wrapper.xml
			implicit def wrapper2string(wrapper:XmlWrapper):String = wrapper.toString
			
			implicit def string2stanza(xml:String):Stanza[_] = StanzaFactory.create(xml)
			implicit def node2stanza(xml:Node):Stanza[_] = StanzaFactory.create(xml)
			
			implicit def node2String(xml:Node):String = xml.toString
			implicit def string2node(xml:String):Node = XML.loadString(xml)
		}
	}
}