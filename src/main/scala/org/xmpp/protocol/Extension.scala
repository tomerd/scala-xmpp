package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._

		final object Extension
		{
			def apply(name:String, namespace:Option[String], children:Option[Seq[Node]]=None):Extension =
			{
				val kids:Seq[Node] = if (!children.isEmpty) children.get else null
				var metadata:MetaData = if (!namespace.isEmpty) new UnprefixedAttribute("xmlns", Text(namespace.get), Null) else Null
				return Extension(Elem(null, name, metadata, TopScope, kids:_*))				
			}
			
			def apply(xml:Node):Extension = new Extension(xml)
		}
		
		class Extension(xml:Node) extends XmlWrapper(xml)
		{			
			parse
			
			// getters
			private var _name:String = null
			private def name:String = _name
			
			private var _namespace:Option[String] = None
			private def namespace:Option[String] = _namespace
			
			// TODO: test this
			protected def parse
			{				
				val _name = this.xml.label
									
				val namespace = (this.xml \ "@xmlns").text
				_namespace = if (namespace.isEmpty) None else Some(namespace)
			}			
			
		}
	}
}