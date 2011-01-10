package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._

		object Extension
		{
			def apply(name:String, namespace:String):Extension = apply(name, namespace, Null, Nil)
			
			def apply(name:String, namespace:String, children:Seq[Node]):Extension = apply(name, namespace, Null, children)
			
			def apply(name:String, namespace:String, attributes:MetaData):Extension = apply(name, namespace, attributes, Nil)
			
			def apply(name:String, namespace:String, attributes:MetaData, children:Seq[Node]):Extension = apply(build(name, namespace, attributes, children))
					
			def apply(other:Extension, children:Seq[Node]=Nil):Extension = apply(build(other.xml.label, other.xml.scope.uri, other.xml.attributes, children))
					
			def apply(xml:Node):Extension = new Extension(xml)
						
			def build(name:String, namespace:String, attributes:Option[MetaData]=None, children:Option[Seq[Node]]=None):Node =
			{
				val scope = new NamespaceBinding(null, namespace, TopScope)
				return Elem(null, name, attributes.getOrElse(Null), scope, children.getOrElse(Nil):_*)
			}			
		}
		
		class Extension(xml:Node) extends XmlWrapper(xml)
		{					
			// getters
			val name:String =  this.xml.label
			
			val namespace:Option[String] = 
			{
				val namespace = this.xml.scope.uri
				if ((null != namespace) && !namespace.isEmpty) Some(namespace) else None
			}
		}
	}
}