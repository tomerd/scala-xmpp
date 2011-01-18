package org.xmpp
{
	package protocol
	{
		import scala.xml._
		
		object Item
		{
			val tag = "item"
			
			def apply(attributes:MetaData, children:Seq[Node]=Nil):Item = apply(build(attributes, children))
			
			//def apply(attributes:Seq[Tuple2[String, String]]):Item = apply(build(attributes, Nil))
			
			//def apply(attributes:Seq[Tuple2[String, String]], children:Seq[Node]):Item = apply(build(attributes, children))
			
			def apply(xml:Node):Item = new Item(xml)
			
			/*
			def build(attributes:Seq[Tuple2[String, String]], children:Seq[Node]):Node = 
			{
				var metadata:MetaData = Null
				attributes.foreach( tuple => metadata = metadata.append(new UnprefixedAttribute(tuple._1, Text(tuple._2), Null)) )
				return build(metadata, children)
			}
			*/
			
			def build(attributes:MetaData, children:Seq[Node]=Nil):Node = Elem(null, tag, attributes, TopScope, children:_*)
		}
		
		class Item (xml:Node) extends XmlWrapper(xml)
		{
		}
	}
}
