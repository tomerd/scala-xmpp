package org.simbit.xmpp
{
	package protocol.extensions.pubsub
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
		
		object Pubsub
		{
			def tag = "pubsub"
			
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
				
			def apply(xml:Node):Pubsub = new Pubsub(xml)
		}
		
		class Pubsub(xml:Node) extends Extension(xml) 
		{
		}

	}
}