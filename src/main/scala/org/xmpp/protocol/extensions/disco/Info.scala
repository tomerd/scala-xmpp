package org.xmpp
{
	package protocol.extensions.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object Info 
		{
			def apply(node:Option[String]=None):Info = 
			{
				val attributes:MetaData = if (!node.isEmpty) new UnprefixedAttribute("node", Text(node.get), Null) else Null
				return apply(InfoBuilder.build(attributes))
			}
			
			def apply(xml:Node):Info = new Info(xml)
		}
		
		class Info(xml:Node) extends Query(xml)
		{
			val node:Option[String] = (this.xml \ "@node").text
			
			def result(identity:Identity, feature:Feature):InfoResult = InfoResult(this.node, List(identity), List(feature))
			
			def result(identity:Identity, features:Seq[Feature]):InfoResult = InfoResult(this.node, List(identity), features)
			
			def result(identities:Seq[Identity], features:Seq[Feature]):InfoResult = InfoResult(this.node, identities, features)
		}
		
	}
}
