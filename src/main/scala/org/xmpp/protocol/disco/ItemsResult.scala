package org.xmpp
{
	package protocol.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		import org.xmpp.protocol.Protocol._
		
		object ItemsResult extends ExtendedStanzaBuilder[ItemsResult]
		{
			val NAMESPACE = "http://jabber.org/protocol/disco#items"
				
			val kind = "result"
			val name = Query.NAME
			val namespace = NAMESPACE			
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], items:Seq[Item]):ItemsResult = 
			{				
				val extension = Query(NAMESPACE, items)
				val xml = Result.build(id, to, from, Some(List(extension)))
				return apply(xml)	
			}
			
			def apply(xml:Node):ItemsResult = new ItemsResult(xml)
		}
		
		class ItemsResult(xml:Node) extends Result(xml)
		{
		}
		
	}
}
