package org.xmpp
{
	package protocol
	{
		import scala.xml._
		
		object Item
		{
			val TAG = "item"
			
			def apply(metadata:MetaData, children:Seq[Node]):Item = apply(Elem(null, TAG, metadata, TopScope, children:_*))
				
			def apply(xml:Node):Item = new Item(xml)	
		}
		
		class Item (xml:Node) extends XmlWrapper(xml)
		{
		}
	}
}
