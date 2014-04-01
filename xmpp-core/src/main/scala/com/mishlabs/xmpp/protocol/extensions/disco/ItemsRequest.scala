package com.mishlabs.xmpp
{
	package protocol.extensions.disco
	{
		import scala.collection._
		import scala.xml._
		
		import com.mishlabs.xmpp.protocol._
		import com.mishlabs.xmpp.protocol.iq._
		import com.mishlabs.xmpp.protocol.extensions._
		
		import com.mishlabs.xmpp.protocol.Protocol._
		
		object ItemsRequest 
		{
			def apply(node:Option[String]=None):ItemsRequest = 
			{
				val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
				return apply(ItemsBuilder.build(attributes))
			}
			
			def apply(xml:Node):ItemsRequest = new ItemsRequest(xml)
		}
		
		class ItemsRequest(xml:Node) extends Query(xml)
		{
			val node:Option[String] = (this.xml \ "@node").text
			
			def result(items:Seq[Item]):ItemsResult = ItemsResult(this.node, items) 
		}
		
	}
}
