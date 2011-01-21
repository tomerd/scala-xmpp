package org.xmpp
{
	package protocol.extensions.muc
	{
		package owner
		{
			import scala.collection._
			import scala.xml._
		
			import org.xmpp.protocol._
			import org.xmpp.protocol.message._
			import org.xmpp.protocol.extensions._
			import org.xmpp.protocol.extensions.muc._
		
			import org.xmpp.protocol.Protocol._
		
			object Builder extends ExtensionBuilder[Query]
			{
				val name = Query.name
				val namespace = org.xmpp.protocol.extensions.muc.Builder.namespace + "#owner"
				
				// FIXME: implement this
				// FIXME: try to find a nicer way to do this, MUC standard is quite dirty
				def apply(xml:Node):Query = 
				{
					if (1 == (xml \ forms.Builder.name).length)
					{
						return RoomConfiguration(xml)
					}
					else
					{
						return Query(xml)
					}
				}
					
			}
		}
	}
}
