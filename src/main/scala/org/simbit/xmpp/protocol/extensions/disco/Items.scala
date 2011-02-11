package org.simbit.xmpp
{
	package protocol.extensions.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.iq._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object Items 
		{
			def apply(node:Option[String]=None):Items = 
			{
				val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
				return apply(ItemsBuilder.build(attributes))
			}
			
			def apply(xml:Node):Items = new Items(xml)
		}
		
		class Items(xml:Node) extends Query(xml)
		{
			val node:Option[String] = (this.xml \ "@node").text
			
			def result(items:Seq[Item]):ItemsResult = ItemsResult(this.node, items) 
		}
		
	}
}
