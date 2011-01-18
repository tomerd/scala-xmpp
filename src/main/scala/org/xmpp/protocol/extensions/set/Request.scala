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
			
			def apply[T](after:T, max:Int):Request = 
			{
				val children = mutable.ListBuffer[Node]()
				children += <after>{ after.toString }</after>
				children += <max>{ max }</max>
				apply(build(children))
			}
			
			def apply(xml:Node):Request = new Request(xml)
		}
		
		class Request (xml:Node) extends Extension(xml)
		{
		}
	}
}



