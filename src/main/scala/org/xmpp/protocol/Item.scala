package org.xmpp
{
	package protocol
	{
		import scala.xml._
		
		object Item
		{
			val TAG = "item"
			
			def apply(metadata:MetaData, children:Seq[Node]):Item = apply(Elem(null, TAG, metadata, TopScope, children:_*))
			
			def apply(tuples:Seq[Tuple2[String, String]]):Item = 
			{
				var metadata:MetaData = Null
				tuples.foreach( tuple => metadata = metadata.append(new UnprefixedAttribute(tuple._1, Text(tuple._2), Null)) )
				return apply(Elem(null, TAG, metadata, TopScope))
			}
				
			def apply(xml:Node):Item = new Item(xml)	
		}
		
		class Item (xml:Node) extends XmlWrapper(xml)
		{
		}
	}
}
