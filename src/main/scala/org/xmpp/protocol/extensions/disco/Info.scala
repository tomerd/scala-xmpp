package org.xmpp
{
	package protocol.extensions.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		
		import org.xmpp.protocol.Protocol._
		
		object Info extends ExtendedStanzaBuilder[Info]
		{
			val kind = Get.kindName
			val name = Query.name
			val namespace = "http://jabber.org/protocol/disco#info"
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], node:Option[String]=None):Info = 
			{
				val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
				val xml = Get.build(id, to, from, Query(namespace, attributes))
				return apply(xml)
			}
			
			def apply(xml:Node):Info = new Info(xml)
		}
		
		class Info(xml:Node) extends Get(xml)
		{
			val node:Option[String] = 
			{
				val node = (this.xml \ "@node").text
				if (node.isEmpty) None else Some(node)
			}
			
			def result(identity:Identity, feature:Feature):InfoResult = InfoResult(this.id, this.from, this.to, this.node, List(identity), List(feature))
			
			def result(identity:Identity, features:Seq[Feature]):InfoResult = InfoResult(this.id, this.from, this.to, this.node, List(identity), features)
			
			def result(identities:Seq[Identity], features:Seq[Feature]):InfoResult = InfoResult(this.id, this.from, this.to, this.node, identities, features)
		}
		
	}
}
