package org.xmpp
{
	package protocol.extensions.muc
	{
		package general
		{
			import scala.collection._
			import scala.xml._
			
			import org.xmpp.protocol._
			import org.xmpp.protocol.message._
			import org.xmpp.protocol.extensions._
			
			import org.xmpp.protocol.Protocol._
			
			object Builder extends ExtensionBuilder[X]
			{
				val name = X.name
				val namespace = "http://jabber.org/protocol/muc"
					
				// FIXME: try to find a nicer way to do this, MUC standard is quite dirty
				def apply(xml:Node):X =
				{
					if (1 == (xml \ History.tag).length)
					{
						return History(xml)
					}
					else
					{
						return RoomPresence(xml)
					}
				}
			}
		}
	}
}
