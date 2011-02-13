package org.simbit.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._

		abstract class XmlWrapper(val xml:Node)
		{				
			override def toString = xml.toString			
		}
			
		object Protocol
		{			
			implicit def string2opt(string:String):Option[String] = if ((null != string) && (!string.isEmpty)) Some(string) else None
			implicit def jid2opt(jid:JID):Option[JID] = if (null != jid) Some(jid) else None		

			implicit def string2seqstring(string:String):Seq[String] = List(string)
			implicit def int2seqint(int:Int):Seq[Int] = List(int)
			implicit def jid2seqjid(jid:JID):Seq[JID] = List(jid)
			
			implicit def seqstring2optseqstring(seq:Seq[String]):Option[Seq[String]] = if ((null != seq) && (!seq.isEmpty)) Some(seq) else None
			implicit def seqint2optseqint(seq:Seq[Int]):Option[Seq[Int]] = if ((null != seq) && (!seq.isEmpty)) Some(seq) else None			
			
			//implicit def string2optseqstring(string:String):Option[Seq[String]] = Some(List(string))
			//implicit def int2optseqint(int:Int):Option[Seq[Int]] = Some(List(int))
			//implicit def jid2optseqjid(jid:JID):Option[Seq[JID]] = Some(List(jid))
			
			implicit def optjid2optstring(optjid:Option[JID]):Option[String] = if (!optjid.isEmpty) optjid.get.toString else None		
			implicit def optbool2optstring(optbool:Option[Boolean]):Option[String] = if (!optbool.isEmpty) optbool.get.toString else None
						
			implicit def metadata2opt(metadata:MetaData):Option[MetaData] = if ((null != metadata) && (Null != metadata)) Some(metadata) else None
			implicit def seqnode2optseqnode(seq:Seq[Node]):Option[Seq[Node]] = if ((null != seq) && (!seq.isEmpty)) Some(seq) else None
						
			implicit def streamhead2string(head:StreamHead):String = head.toString
			implicit def streamtail2string(tail:StreamTail):String = tail.toString
			implicit def wrapper2string(wrapper:XmlWrapper):String = wrapper.toString
			implicit def wrapper2node(wrapper:XmlWrapper):Node = wrapper.xml
			implicit def wrapper2seq(wrapper:XmlWrapper):Seq[XmlWrapper] = List(wrapper)			
			implicit def wrapper2opt(wrapper:XmlWrapper):Option[XmlWrapper] = if (null != wrapper) Some(wrapper) else None
			
			implicit def seqwrapper2opt(seq:Seq[XmlWrapper]):Option[Seq[XmlWrapper]] = if ((null != seq) && (!seq.isEmpty)) Some(seq) else None
			implicit def wrapper2optseq(wrapper:XmlWrapper):Option[Seq[XmlWrapper]] = if (null != wrapper) Some(List(wrapper)) else None			
			implicit def wrapper2optseqnode(wrapper:XmlWrapper):Option[Seq[Node]] = if (null != wrapper) Some(List(wrapper)) else None
			implicit def optwrapper2optseqnode(optwrapper:Option[XmlWrapper]):Option[Seq[Node]] = if (!optwrapper.isEmpty) Some(List(optwrapper.get)) else None
			implicit def seqwrapper2optseqnode(seq:Seq[XmlWrapper]):Option[Seq[Node]] = if ((null != seq) && (!seq.isEmpty)) Some(seq) else None
									
			implicit def seqoptjid2seqoptstring(seqoptjid:Option[Seq[JID]]):Option[Seq[String]] = if (!seqoptjid.isEmpty) seqoptjid.get.map( jid => jid.toString ) else None
			
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
			
			implicit def tuples2metadata(tuple:Tuple2[String, String]):MetaData = new UnprefixedAttribute(tuple._1, Text(tuple._2), Null)			
			implicit def seqtuples2metadata(tuples:Seq[Tuple2[String, String]]):MetaData = 
			{
				var metadata:MetaData = Null
				tuples.foreach( tuple => metadata = metadata.append(new UnprefixedAttribute(tuple._1, Text(tuple._2), Null)) )
				return metadata
			}			
						
			// TODO: not sure why these are required and the wrapper is not enough
			implicit def ext2optext(ext:Extension):Option[Extension] = if (null != ext) Some(ext) else None
			implicit def ext2seqoptext(ext:Extension):Option[Seq[Extension]] = if (null != ext) Some(List(ext)) else None
			implicit def seqext2optseqext(seq:Seq[Extension]):Option[Seq[Extension]] = if ((null != seq) && (!seq.isEmpty)) Some(seq) else None	
			
			implicit def string2stanza(xml:String):Stanza = Stanza(xml)
			implicit def node2stanza(xml:Node):Stanza = Stanza(xml)
			
			implicit def node2String(xml:Node):String = xml.toString
			implicit def string2node(xml:String):Node = XML.loadString(xml)
			
			implicit def jid2string(jid:JID):String = jid.toString
			implicit def string2jid(string:String) = JID(string)
		}
	}
}