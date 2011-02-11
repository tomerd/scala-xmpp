package org.simbit.xmpp
{
	package protocol.extensions.muc
	{
		package general
		{
			import scala.collection._
			import scala.xml._
			
			import org.simbit.xmpp.protocol._
			import org.simbit.xmpp.protocol.presence._
			import org.simbit.xmpp.protocol.extensions._
			
			import org.simbit.xmpp.protocol.Protocol._
			
			object RoomPresence
			{				
				def apply():RoomPresence = apply(Builder.build())
					
				def apply(xml:Node):RoomPresence = new RoomPresence(xml)			
			}
			
			class RoomPresence(xml:Node) extends X(xml)
			{	
			}
		}
	}	
}