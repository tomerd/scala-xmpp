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
		
		object Builder extends ExtensionBuilder[X]
		{
			val name = X.name
			val namespace = "http://jabber.org/protocol/muc"
				
			// TODO: look into this, seems like a single stanza is mapped to this namespace
			def apply(xml:Node):X = RoomPresence(xml)
		}
		
	}
}
