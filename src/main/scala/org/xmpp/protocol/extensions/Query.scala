package org.xmpp
{
	package protocol.extensions
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Query // extends ExtensionB[Query]
		{
			def name = "query"
			
			/*
			def apply(namespace:String):Query = apply(namespace, Null, Nil)
			
			def apply(namespace:String, children:Seq[Node]):Query = apply(namespace, Null, children)
			
			def apply(namespace:String, attributes:MetaData):Query = apply(namespace, attributes, Nil)
			
			def apply(namespace:String, attributes:MetaData, children:Seq[Node]):Query = apply(build(namesapce, attributes, children))
			
			def build(namespace:String):Node = Extension.build(name, namespace, Null, Nil)
			
			def build(namespace:String, children:Seq[Node]):Node = Extension.build(name, namespace, Null, children)
			
			def build(namespace:String, attributes:MetaData):Node = Extension.build(name, namespace, attributes, Nil)
			
			def build(namespace:String, attributes:MetaData, children:Seq[Node]):Node = Extension.build(name, namespace, attributes, children)
			*/
				
			def apply(xml:Node):Query = new Query(xml)
		}
		
		class Query(xml:Node) extends Extension(xml) 
		{
		}

	}
}