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
		
		object Items extends ExtendedStanzaBuilder[Items]
		{
			val stanzaType = Get.stanzaTypeName
			val name = Query.name
			val namespace = "http://jabber.org/protocol/disco#items"
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], node:Option[String]=None):Items = 
			{
				val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
				val xml = Result.build(id, to, from, Query(namespace, attributes))
				return apply(xml)
			}
			
			def apply(xml:Node):Items = new Items(xml)
		}
		
		class Items(xml:Node) extends Get(xml)
		{
			val node:Option[String] = (this.xml \ "@node").text
			
			def result(items:Seq[Item]):ItemsResult = ItemsResult(this.id, this.from, this.to, this.node, items) 
		}
		
	}
}
