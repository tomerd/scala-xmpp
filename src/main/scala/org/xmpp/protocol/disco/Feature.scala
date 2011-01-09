package org.xmpp
{
	package protocol.disco
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		import org.xmpp.protocol.Protocol._
		
		object Feature
		{		
			val TAG = "feature"
			
			def apply(namespace:String):Feature = 
			{				
				val metadata = new UnprefixedAttribute("var", Text(namespace), Null)
				return apply(Elem(null, TAG, metadata, TopScope))
			}
			
			def apply(xml:Node):Feature = new Feature(xml)
		}
		
		class Feature(xml:Node) extends XmlWrapper(xml)
		{
		}
	}
}
