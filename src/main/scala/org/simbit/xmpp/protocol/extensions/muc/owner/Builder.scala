package org.simbit.xmpp
{
	package protocol.extensions.muc
	{
		package owner
		{
			import scala.collection._
			import scala.xml._
		
			import org.simbit.xmpp.protocol._
			import org.simbit.xmpp.protocol.message._
			import org.simbit.xmpp.protocol.extensions._
			import org.simbit.xmpp.protocol.extensions.muc._
		
			import org.simbit.xmpp.protocol.Protocol._
		
			private[xmpp] object Builder extends ExtensionBuilder[Query]
			{
				val tag = Query.tag
				val namespace = org.simbit.xmpp.protocol.extensions.muc.general.Builder.namespace + "#owner"
				
				// FIXME: try to find a nicer way to do this, MUC standard is quite dirty
				def apply(xml:Node):Query = 
				{
					if (1 == (xml \ Destroy.tag).length)
					{
						return Destroy(xml)
					}
					else if (1 == (xml \ forms.Builder.tag).length)
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

