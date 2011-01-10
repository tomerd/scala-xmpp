package org.xmpp
{
	package protocol.extensions.disco
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
						
			def apply(id:Option[String], to:Option[JID], from:Option[JID], identities:Seq[Identity], features:Seq[Feature]):InfoResult = apply(id, to, from, None, identities, features)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], node:Option[String], identities:Seq[Identity], features:Seq[Feature]):InfoResult = 
			{
				val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
				val xml = Result.build(id, to, from, Query(namespace, attributes, identities ++ features))
				return apply(xml)
			}
			
			def apply(xml:Node):InfoResult = new InfoResult(xml)
		}
		
		class InfoResult(xml:Node) extends Result(xml)
		{
		}
		
	}
}
