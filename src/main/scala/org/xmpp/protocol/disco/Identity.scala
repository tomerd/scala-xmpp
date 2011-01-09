package org.xmpp
{
	package protocol.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		import org.xmpp.protocol.Protocol._
		
		object Identity
		{
			val tag = "identity"
			
			def apply(category:String, kind:String, name:Option[String]):Identity = 
			{
				var metadata:MetaData = new UnprefixedAttribute("category", Text(category), Null)
				metadata = metadata.append(new UnprefixedAttribute("type", Text(kind), Null))
				if (!name.isEmpty) metadata = metadata.append(new UnprefixedAttribute("name", Text(name.get), Null)) 
				
				return apply(Elem(null, tag, metadata, TopScope))
			}
			
			def apply(xml:Node):Identity = new Identity(xml)
		}
		
		class Identity(xml:Node) extends XmlWrapper(xml)
		{
		}
		
	}
}
