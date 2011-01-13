package org.xmpp
{
	package protocol.extensions.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object Items extends ExtensionBuilder[Items]
		{
			val name = Query.name
			val namespace = "http://jabber.org/protocol/disco#items"
			
			def apply(node:Option[String]=None):Items = 
			{
				val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
				return apply(build(attributes))
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
