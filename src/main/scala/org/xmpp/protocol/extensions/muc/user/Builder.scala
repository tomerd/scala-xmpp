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
				
				// FIXME: implement this
				def apply(xml:Node):X = 
				{
					(xml \ "inivite").length match
					{
						case 1 => Invite(xml)
						case _ =>
						{
							RoomPresenceBroadcast(xml)
						}
					}
					
				}
			}
		}
	}
}
