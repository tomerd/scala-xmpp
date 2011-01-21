package org.xmpp
{
	package protocol.extensions.muc
	{
		package user
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
				val namespace = org.xmpp.protocol.extensions.muc.Builder.namespace + "#user"
				
				// FIXME: try to find a nicer way to do this, MUC standard is quite dirty
				def apply(xml:Node):X = 
				{
					if (1 == (xml \ Invite.tag).length)
					{
						return Invite(xml)
					}
					else if (1 == (xml \ Decline.tag).length)
					{
						return Decline(xml)
					}
					else if (1 == (xml \ Item.tag).length)
					{
						return RoomPresenceBroadcast(xml)
					}					
					else
					{
						return X(xml)
					}
				}
			}
		}
	}
}
