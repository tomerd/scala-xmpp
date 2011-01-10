package org.xmpp
{
	package protocol.extensions
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object X
		{
			def name = "x"
			
			def apply(namespace:String):Query = apply(namespace, Null, Nil)
			
			def apply(namespace:String, children:Seq[Node]):Query = apply(namespace, Null, children)
			
			def apply(namespace:String, attributes:MetaData):Query = apply(namespace, attributes, Nil)
			
			def apply(namespace:String, attributes:MetaData, children:Seq[Node]):Query = apply(Extension.build(name, namespace, attributes, children))
			
			def apply(xml:Node):Query = new Query(xml)
		}
		
		class X(xml:Node) extends Extension(xml) 
		{
		}
	}
}