package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._

		final object Extension
		{
			def apply(name:String, namespace:Option[String], attributes:Option[MetaData]=None):Extension =
			{
				var metadata:MetaData = Null
				if (!namespace.isEmpty) metadata = metadata.append(new UnprefixedAttribute("xmlns", Text(namespace.get), Null))
				if (!attributes.isEmpty) metadata =  metadata.append(attributes.get)
				return new Extension(Elem(null, name, metadata, TopScope))				
			}
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