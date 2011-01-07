package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._

		object Extension
		{
			def apply(name:String, namespace:Option[String], children:Option[Seq[Node]]=None):Extension =
			{
				val kids:Seq[Node] = if (!children.isEmpty) children.get else null
				val scope = if (!namespace.isEmpty) new NamespaceBinding(null, namespace.get, TopScope) else TopScope
				return Extension(Elem(null, name, Null, scope, kids:_*))
			}
			
			def apply(other:Extension, children:Option[Seq[Node]]):Extension =
			{
				val kids:Seq[Node] = if (!children.isEmpty) children.get else null
				return Extension(Elem(null, other.xml.label, other.xml.attributes, other.xml.scope, kids:_*))
			}
			
			def apply(xml:Node):Extension = new Extension(xml)
		}
		
		class Extension(xml:Node) extends XmlWrapper(xml)
		{					
			// getters
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
		}
	}
}