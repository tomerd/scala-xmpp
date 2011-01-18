package org.xmpp
{
	package protocol.extensions.set
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object Request extends ExtensionBuilder[Request]
		{
			val name = "set"
			val namespace = "http://jabber.org/protocol/rsm"
			
			def apply[T](max:Int, before:Option[T], after:Option[T]):Request = 
			{
				val children = mutable.ListBuffer[Node]()
				children += <max>{ max }</max>
				if (!before.isEmpty) children += <before>{ before.get.toString }</before>
				if (!after.isEmpty) children += <after>{ after.get.toString }</after>				
				apply(build(children))
			}
			
			def apply(xml:Node):Request = new Request(xml)
		}
		
		class Request (xml:Node) extends Extension(xml)
		{
		}
	}
}



