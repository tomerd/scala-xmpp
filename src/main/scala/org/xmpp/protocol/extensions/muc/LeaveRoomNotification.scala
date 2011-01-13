package org.xmpp.protocol
{
	package extensions.muc
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.presence._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._		
		
		
		object LeaveRoomResult extends ExtensionBuilder[LeaveRoomResult]
		{
			val name = X.name
			val namespace = "http://jabber.org/protocol/muc#user"
				
			def apply(affiliation:RoomAffiliation.Value, role:RoomRole.Value, statuses:Option[Seq[Int]]):LeaveRoomResult =
			{
				val children = mutable.ListBuffer[Node]()
				children += <item affiliation={  affiliation.toString } role={ role.toString }  />
				if (!statuses.isEmpty) statuses.foreach ( status => children += <status code={ status.toString } /> )
				return apply(build(children))
			}
				
			def apply(xml:Node):LeaveRoomResult = LeaveRoomResult(xml)			
		}
		
		class LeaveRoomResult(xml:Node) extends X(xml)
		{				
			val affiliation:RoomAffiliation.Value = RoomAffiliation.withName((this.xml \ "@affiliation").text)
			
			val role:RoomRole.Value = RoomRole.withName((this.xml \ "@role").text)

			val statuses:Option[Seq[Int]] = 
			{
				val nodes = (xml \ "status")
				nodes.length match
				{
					case 0 => None
					case _ => Some(nodes.map ( node => node.text.toInt ))
				}
			}
		}
		
		
		
	}	
}