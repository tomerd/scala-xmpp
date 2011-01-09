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
			val kind = Result.kindName
			val name = Query.name
			val namespace = Info.namespace
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], identify:Identity, feature:Feature):InfoResult = apply(id, to, from, identify, List(feature)) 
						
			def apply(id:Option[String], to:Option[JID], from:Option[JID], identify:Identity, features:Seq[Feature]):InfoResult = 
			{
				val xml = Result.build(id, to, from, Query(namespace, List(identify) ++ features))
				return apply(xml)
			}
			
			def apply(xml:Node):InfoResult = new InfoResult(xml)
		}
		
		class InfoResult(xml:Node) extends Result(xml)
		{
		}
		
	}
}
