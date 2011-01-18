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
				
				def apply(to:JID, reason:Option[String], password:Option[String], thread:Option[String]):Invite = 
				{
					val children = mutable.ListBuffer[Node]()
					if (!reason.isEmpty) children += <reason>{ reason.get }</reason>
					if (!password.isEmpty) children += <password>{ password.get }</password>
					if (!thread.isEmpty) children += <continue thread={ thread.get } />
					
					val metadata = new UnprefixedAttribute("to", Text(to), Null)
					
					apply(Builder.build(Elem(null, tag, metadata, TopScope, children:_*)))
				}
				
				def apply(xml:Node):Invite = Invite(xml)
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
				
				val password:Option[String] = 
				{
					val nodes = (this.xml \ "password")
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