package org.xmpp
{
	package protocol.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		import org.xmpp.protocol.Protocol._
		
		object Items extends ExtendedStanzaBuilder[Items]
		{
			val NAMESPACE = "http://jabber.org/protocol/disco#items"
				
			val kind = "get"
			val name = Query.NAME
			val namespace = NAMESPACE			
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], items:Option[Seq[Item]]):Items = 
			{				
				val extension = Query(NAMESPACE, items)
				val xml = Result.build(id, to, from, Some(List(extension)))
				return apply(xml)	
			}
			
			def apply(xml:Node):Items = new Items(xml)
		}
		
		class Items(xml:Node) extends Get(xml)
		{
			def result(items:Seq[Item]):ItemsResult = ItemsResult(this.id, this.from, this.to, items) 
		}
		
	}
}
