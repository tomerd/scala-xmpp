package org.xmpp
{
	package protocol.extensions.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.message._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object ItemsBuilder extends ExtensionBuilder[Query]
		{
			val name = Query.name
			val namespace = "http://jabber.org/protocol/disco#items"
			
			def apply(xml:Node):Query = 
			{
				(xml \ "item").length match
				{
					case 0 => Items(xml)
					case _ => ItemsResult(xml)
				}
			}
		}
		
	}
}
