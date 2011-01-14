package org.xmpp
{
	package protocol.extensions.muc
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.message._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object AdminBuilder extends ExtensionBuilder[X]
		{
			val name = X.name
			val namespace = Builder.namespace + "#admin"
				
			// FIXME: implement this
			def apply(xml:Node):X = X(xml)
			
		}
		
	}
}
