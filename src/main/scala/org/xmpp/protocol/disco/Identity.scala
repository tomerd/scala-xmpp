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
			val TAG = "identity"
			
			def apply(name:String, category:String, kind:String):Identity = 
			{				
				var metadata:MetaData = new UnprefixedAttribute("name", Text(name), Null)
				metadata = metadata.append(new UnprefixedAttribute("category", Text(category), Null))
				metadata = metadata.append(new UnprefixedAttribute("type", Text(kind), Null))				
				return apply(Elem(null, TAG, metadata, TopScope))
			}
			
			def apply(xml:Node):Identity = new Identity(xml)
		}
		
		class Identity(xml:Node) extends XmlWrapper(xml)
		{
		}
		
	}
}
