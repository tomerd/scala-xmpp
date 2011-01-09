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
			val kind = Result.kindName
			val name = Query.name
			val namespace = Items.namespace
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], items:Seq[Item]):ItemsResult = 
			{	
				val xml = Result.build(id, to, from, Query(namespace, items))
				return apply(xml)
			}
			
			def apply(xml:Node):ItemsResult = new ItemsResult(xml)
		}
		
		class ItemsResult(xml:Node) extends Result(xml)
		{
		}
		
	}
}
