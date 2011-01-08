package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._

		abstract class XmlWrapper(val xml:Node)
		{	
			/*
			parse
			
			protected def parse
			{				
			}
			*/
			
			override def toString = xml.toString			
		}
			
		object Protocol
		{			
			implicit def string2opt(string:String):Option[String] = if ((null != string) && (!string.isEmpty)) Some(string) else None
			implicit def jid2opt(jid:JID):Option[JID] = if (null != jid) Some(jid) else None
			implicit def wrapper2opt(wrapper:XmlWrapper):Option[XmlWrapper] = if (null != wrapper) Some(wrapper) else None
			implicit def metadata2opt(metadata:MetaData):Option[MetaData] = if ((null != metadata) && (Null != metadata)) Some(metadata) else None
			implicit def seqwrapper2opt(seq:Seq[XmlWrapper]):Option[Seq[XmlWrapper]] = if ((null != seq) && (!seq.isEmpty)) Some(seq) else None
			implicit def wrapper2optseq(wrapper:XmlWrapper):Option[Seq[XmlWrapper]] = if (null != wrapper) Some(List(wrapper)) else None
			implicit def wrapper2optseqnode(wrapper:XmlWrapper):Option[Seq[Node]] = if (null != wrapper) Some(List(wrapper)) else None
			implicit def optwrapper2optseqnode(optwrapper:Option[XmlWrapper]):Option[Seq[Node]] = if (!optwrapper.isEmpty) Some(List(optwrapper.get)) else None
			implicit def seqwrapper2optseqnode(seq:Seq[XmlWrapper]):Option[Seq[Node]] = if ((null != seq) && (!seq.isEmpty)) Some(seq) else None
			implicit def seqnode2optseqnode(seq:Seq[Node]):Option[Seq[Node]] = if ((null != seq) && (!seq.isEmpty)) Some(seq) else None			
			
			implicit def wrapper2node(wrapper:XmlWrapper):Node = wrapper.xml
			implicit def wrapper2string(wrapper:XmlWrapper):String = wrapper.toString
						
			implicit def seqwrapper2seqnode(seq:Seq[XmlWrapper]):Seq[Node] = 
			{
				val nodes = new mutable.ListBuffer[Node]()
				seq.foreach( node => nodes += node )
				return nodes
			}
			
			implicit def optseqwrapper2optseqnode(optseq:Option[Seq[XmlWrapper]]):Option[Seq[Node]] = 
			{
				optseq match
				{
					case None => None
					case Some(seq) =>
					{
						val nodes = new mutable.ListBuffer[Node]()
						seq.foreach( node => nodes += node )
						return Some(nodes)
					}
				}
			}
			
			implicit def wrapper2seq(wrapper:XmlWrapper):Seq[XmlWrapper] = List(wrapper)
								
			implicit def string2stanza(xml:String):Stanza = Stanza(xml)
			implicit def node2stanza(xml:Node):Stanza = Stanza(xml)
			
			implicit def node2String(xml:Node):String = xml.toString
			implicit def string2node(xml:String):Node = XML.loadString(xml)
			
			implicit def jid2string(jid:JID):String = jid.toString
			implicit def string2jid(string:String) = JID(string)
		}
	}
}