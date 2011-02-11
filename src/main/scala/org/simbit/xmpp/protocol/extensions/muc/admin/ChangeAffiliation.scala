package org.simbit.xmpp
{
	package protocol.extensions.muc	
	{
		package admin
		{
			import scala.collection._
			import scala.xml._
			
			import org.simbit.xmpp.protocol._
			import org.simbit.xmpp.protocol.presence._
			import org.simbit.xmpp.protocol.extensions._
			import org.simbit.xmpp.protocol.extensions.muc._
			
			import org.simbit.xmpp.protocol.Protocol._		
					
			object ChangeAffiliation
			{			
				def apply(nick:String, affiliation:Affiliation.Value, reason:Option[String]):ChangeRole =
				{
					val itemChildren = mutable.ListBuffer[Node]()
					if (!reason.isEmpty) itemChildren += <reason>{ reason }</reason>
					
					var itemMetadata:MetaData = new UnprefixedAttribute("nick", Text(nick), Null)
					itemMetadata = itemMetadata.append(new UnprefixedAttribute("affiliation", Text(affiliation.toString), Null))
					
					val children = mutable.ListBuffer[Node]()
					children += Elem(null, "item", itemMetadata, TopScope, itemChildren:_*)
									
					return apply(Builder.build(children))
				}
				
				def apply(xml:Node):ChangeRole = ChangeRole(xml)
			}
			
			class ChangeAffiliation(xml:Node) extends X(xml)
			{	
				private val itemNode = (xml \ "item")(0)
							
				val nick:String = (this.itemNode \ "@nick").text
				
				val affiliation:Affiliation.Value = Affiliation.withName((this.itemNode \ "@affiliation").text)
	
				val reason:Option[String] = 
				{
					val nodes = (this.itemNode \ "reason")
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