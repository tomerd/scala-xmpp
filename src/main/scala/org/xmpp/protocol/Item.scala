package org.xmpp
{
	package protocol
	{
		import scala.xml._
		
		object Item
		{
			val tag = "item"
			
			def apply(attributes:MetaData, children:Seq[Node]):Item = apply(build(attributes, children))
			
			def apply(attributes:Seq[Tuple2[String, String]]):Item = apply(build(attributes))
			
			def apply(xml:Node):Item = new Item(xml)
			
			def build(attributes:MetaData, children:Seq[Node]):Node = Elem(null, TAG, attributes, TopScope, children:_*)
			
			def build(attributes:Seq[Tuple2[String, String]]):Node = 
			{
				var metadata:MetaData = Null
				attributes.foreach( tuple => metadata = metadata.append(new UnprefixedAttribute(tuple._1, Text(tuple._2), Null)) )
				return Elem(null, tag, metadata, TopScope)
			}
		}
		
		class Item (xml:Node) extends XmlWrapper(xml)
		{
		}
	}
}
