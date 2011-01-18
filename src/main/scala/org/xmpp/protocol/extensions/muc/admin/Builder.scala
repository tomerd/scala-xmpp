package org.xmpp
{
	package protocol.extensions.muc
	{
		package admin
		{
			import scala.collection._
			import scala.xml._
			
			import org.xmpp.protocol._
			import org.xmpp.protocol.message._
			import org.xmpp.protocol.extensions._
			import org.xmpp.protocol.extensions.muc._
			
			import org.xmpp.protocol.Protocol._
			
			object Builder extends ExtensionBuilder[X]
			{
				val name = X.name
				val namespace = org.xmpp.protocol.extensions.muc.Builder.namespace + "#admin"
					
				// FIXME: try to find a nicer way to do this, MUC standard is quite dirty
				def apply(xml:Node):X = 
				{
					if (false)
					{
						return X(xml)
					}
					else
					{
						return ChangeStatus(xml)
					}					
				}
				
			}
		}
	}
}
