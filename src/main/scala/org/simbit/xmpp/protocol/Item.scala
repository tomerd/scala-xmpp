package org.simbit.xmpp
{
	package protocol
	{
		import scala.xml._
		
		object Item
		{
			val tag = "item"
			
			def apply(attributes:MetaData, children:Seq[Node]=Nil):Item = apply(build(attributes, children))
						
			def apply(xml:Node):Item = new Item(xml)
					
			def build(attributes:MetaData, children:Seq[Node]=Nil):Node = Elem(null, tag, attributes, TopScope, children:_*)
		}
		
		class Item (xml:Node) extends XmlWrapper(xml)
		{
		}
	}
}
