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
		
		object ItemsResult extends ExtendedStanzaBuilder[ItemsResult]
		{
			val stanzaType = Result.stanzaTypeName
			val name = Query.name
			val namespace = Items.namespace
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], items:Seq[Item]):ItemsResult = apply(id, to, from, None, items)			
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], node:Option[String], items:Seq[Item]):ItemsResult = 
			{	
				val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
				val xml = Result.build(id, to, from, Query(namespace, attributes, items))
				return apply(xml)
			}
			
			def apply(xml:Node):ItemsResult = new ItemsResult(xml)
		}
		
		class ItemsResult(xml:Node) extends Result(xml)
		{
			val node:Option[String] = (this.xml \ "@node").text
			
			val items:Seq[Item] = (this.xml \ "item").map( node => Item(node) )
		}
		
	}
}
