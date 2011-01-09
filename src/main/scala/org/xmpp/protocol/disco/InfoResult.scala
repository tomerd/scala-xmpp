package org.xmpp
{
	package protocol.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		import org.xmpp.protocol.Protocol._
		
		object InfoResult extends ExtendedStanzaBuilder[InfoResult]
		{
			val NAMESPACE = "http://jabber.org/protocol/disco#info"
				
			val kind = "result"
			val name = Query.NAME
			val namespace = NAMESPACE			
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], identify:Identity, features:Seq[Feature]):InfoResult = 
			{				
				val extension = Query(NAMESPACE, List(identify) ++ features)
				val xml = Result.build(id, to, from, Some(List(extension)))
				return apply(xml)	
			}
			
			def apply(xml:Node):InfoResult = new InfoResult(xml)
		}
		
		class InfoResult(xml:Node) extends Result(xml)
		{
		}
		
	}
}
