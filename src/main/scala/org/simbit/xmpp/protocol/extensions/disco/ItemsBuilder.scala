package org.simbit.xmpp
{
	package protocol.extensions.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.message._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		private[xmpp] object ItemsBuilder extends ExtensionBuilder[Query]
		{
			val tag = Query.tag
			val namespace = "http://jabber.org/protocol/disco#items"
			
			def apply(xml:Node):Query = 
			{
				(xml \ "item").length match
				{
					case 0 => ItemsRequest(xml)
					case _ => ItemsResult(xml)
				}
			}
		}
		
	}
}
