package org.xmpp
{
	package protocol.extensions.muc
	{
		package owner
		{
			import scala.collection._
			import scala.xml._
		
			import org.xmpp.protocol._
			import org.xmpp.protocol.presence._
			import org.xmpp.protocol.extensions._
		
			import org.xmpp.protocol.Protocol._		
					
			object Destroy 
			{
				val tag = "destroy"
				
				def apply(jid:JID, reason:Option[String]):Destroy = 
				{
					val children = mutable.ListBuffer[Node]()
					if (!reason.isEmpty) children += <reason>{ reason }</reason>
					
					val metadata = new UnprefixedAttribute("jid", Text(jid), Null)
					
					apply(Builder.build(Elem(null, tag, metadata, TopScope, children:_*)))
				}
				
				def apply(xml:Node):Destroy = Destroy(xml)
			}
		
			class Destroy(xml:Node) extends Query(xml)
			{
				private val inviteNode = (xml \ Destroy.tag)(0)
				
				val jid:JID = (this.inviteNode \ "@jid").text
				
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