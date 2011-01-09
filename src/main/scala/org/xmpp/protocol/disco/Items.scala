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
			val kind = Get.kindName
			val name = Query.name
			val namespace = "http://jabber.org/protocol/disco#items"
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], items:Option[Seq[Item]]):Items = 
			{
				val xml = Result.build(id, to, from, Query(namespace, items))
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
