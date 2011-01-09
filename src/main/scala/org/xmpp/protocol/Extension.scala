package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._

		object Extension
		{
			def apply(name:String, namespace:Option[String], children:Option[Seq[Node]]=None):Extension = apply(build(name, namespace, None, children))
			
			def apply(other:Extension):Extension = apply(other, None)
			
			def apply(other:Extension, children:Option[Seq[Node]]):Extension = apply(build(other.xml.label, other.xml.scope.uri, other.xml.attributes, children))
					
			def apply(xml:Node):Extension = new Extension(xml)
			
			def build(name:String, namespace:Option[String], attributes:Option[MetaData]=None, children:Option[Seq[Node]]=None):Node =
			{
				val scope = if (!namespace.isEmpty) new NamespaceBinding(null, namespace.get, TopScope) else TopScope
				return Elem(null, name, attributes.getOrElse(Null), scope, children.getOrElse(null):_*)
			}			
		}
		
		class Extension(xml:Node) extends XmlWrapper(xml)
		{					
			// getters
			/*
			private var _name:String = null
			def name:String = _name
			
			private var _namespace:Option[String] = None
			def namespace:Option[String] = _namespace
			
			override protected def parse
			{				
				super.parse 
				
				_name = this.xml.label
									
				val namespace = this.xml.scope.uri
				_namespace = if ((null != namespace) && !namespace.isEmpty) Some(namespace) else None
			}		
			*/
			
			def name:String =  this.xml.label
			
			def namespace:Option[String] = 
			{
				val namespace = this.xml.scope.uri
				if ((null != namespace) && !namespace.isEmpty) Some(namespace) else None
			}
		}
	}
}