package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._
		
		/*
		trait ExtensionB[T <: Extension]
		{
			val name:String
			
			val namespace:String
			
			def apply:T = apply(Null, Nil)
			
			def apply(children:Seq[Node]):T = apply(Null, children)
			
			def apply(attributes:MetaData):T = apply(attributes, Nil)
			
			def apply(attributes:MetaData, children:Seq[Node]):T = apply(build(attributes, children))
			
			//def apply(other:Extension, children:Seq[Node]=Nil):T = apply(build(other.xml.label, other.xml.scope.uri, other.xml.attributes, children))
			
			def apply(xml:Node):T
			
			def build():Node = build(Null, Nil)
			
			def build(children:Seq[Node]):Node = build(Null, children)
			
			def build(attributes:MetaData):Node = build(attributes, Nil)
			
			def build(attributes:MetaData, children:Seq[Node]):Node =
			{
				val scope = new NamespaceBinding(null, this.namespace, TopScope)
				return Elem(null, this.name, attributes, scope, children:_*)
			}
		}
		*/
		
		abstract class Extension(xml:Node) extends XmlWrapper(xml)
		{
			val name:String =  this.xml.label
			
			val namespace:Option[String] = this.xml.scope.uri
		}
	}
}