package org.xmpp
{
	package protocol.extensions.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		import org.xmpp.protocol.Protocol._
		
		object Identity
		{
			val tag = "identity"
			
			def apply(category:String, identityType:String, name:Option[String]=None):Identity = 
			{
				var metadata:MetaData = new UnprefixedAttribute("category", Text(category), Null)
				metadata = metadata.append(new UnprefixedAttribute("type", Text(identityType), Null))
				if (!name.isEmpty) metadata = metadata.append(new UnprefixedAttribute("name", Text(name.get), Null)) 
				
				return apply(Elem(null, tag, metadata, TopScope))
			}
			
			def apply(xml:Node):Identity = new Identity(xml)
		}
		
		class Identity(xml:Node) extends XmlWrapper(xml)
		{
			val category:String = (this.xml \ "@category").text
			
			val identityType:String = (this.xml \ "@type").text
			
			val name:Option[String] = (this.xml \ "@name").text
		}
		
	}
}
