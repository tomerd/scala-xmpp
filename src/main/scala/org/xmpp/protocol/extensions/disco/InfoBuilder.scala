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
		
		object InfoBuilder extends ExtensionBuilder[Query]
		{
			val name = Query.name
			val namespace = "http://jabber.org/protocol/disco#info"
			
			def apply(xml:Node):Query = 
			{
				(xml \ "feature").length match
				{
					case 0 => Info(xml)
					case _ => InfoResult(xml)
				}
			}
		}
		
	}
}
