package org.xmpp
{
	package protocol.extensions.muc
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.presence._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object RoomPresence
		{				
			def apply():RoomPresence = apply(Builder.build())
				
			def apply(xml:Node):RoomPresence = RoomPresence(xml)			
		}
		
		class RoomPresence(xml:Node) extends X(xml)
		{		
		}
	}	
}