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
				
		object JoinRoomNotification extends ExtensionBuilder[JoinRoomNotification]
		{
			val name = X.name
			val namespace = "http://jabber.org/protocol/muc#user"
				
			def apply(affiliation:RoomAffiliation.Value, role:RoomRole.Value, statuses:Option[Seq[Int]]):JoinRoomNotification =
			{
				val children = mutable.ListBuffer[Node]()
				children += <item affiliation={  affiliation.toString } role={ role.toString }  />
				if (!statuses.isEmpty) statuses.foreach ( status => children += <status code={ status.toString } /> )
				return apply(build(children))
			}
				
			def apply(xml:Node):JoinRoomNotification = JoinRoomNotification(xml)			
		}
		
		class JoinRoomNotification(xml:Node) extends X(xml)
		{	
			private val itemNode = (xml \ "item")(0)
			
			val affiliation:RoomAffiliation.Value = RoomAffiliation.withName((this.itemNode \ "@affiliation").text)
			
			val role:RoomRole.Value = RoomRole.withName((this.itemNode \ "@role").text)

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
		
		object RoomAffiliation extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val Owner = Value("owner") 
			val Admin = Value("admin")
			val Member = Value("member")			
		}
		
		object RoomRole extends Enumeration
		{
			type value = Value

			val Unknown = Value("unknown") // internal use
			val Moderator = Value("moderator") 
			val Participant = Value("participant")
			val Visitor = Value("visitor")			
		}		
		
	}	
}