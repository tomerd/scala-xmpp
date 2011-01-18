package org.xmpp
{
	package protocol.extensions.muc
	{
		package user
		{
			import scala.collection._
			import scala.xml._
		
			import org.xmpp.protocol._
			import org.xmpp.protocol.presence._
			import org.xmpp.protocol.extensions._
		
			import org.xmpp.protocol.Protocol._		
			
			object Invite 
			{
				val tag = "invite"
				
				def apply(to:JID, reason:Option[String]):Invite = apply(build(to, reason))
				
				def apply(xml:Node):Invite = Invite(xml)
				
				def build(to:JID, reason:Option[String]):Node =
				{
					val children = mutable.ListBuffer[Node]()
					if (!reason.isEmpty) children += <reason>{ reason }</reason>
					
					val metadata = new UnprefixedAttribute("to", Text(to), Null)

					Builder.build(Elem(null, tag, metadata, TopScope, children:_*))
				}
			}
		
			class Invite(xml:Node) extends X(xml)
			{
				private val inviteNode = (xml \ "invite")(0)
				
				val to:JID = (this.inviteNode \ "@to").text
				
				val reason:Option[String] = 
				{
					val nodes = (this.inviteNode \ "reason")
					nodes.length match
					{
						case 0 => None
						case _ => Some(nodes(0).text)
					}
				}
			}
		}
	}	
}