package org.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object IQFactory extends StanzaFactory[IQ]
		{
			// well known extensions
			registerExtension(disco.Info)
			registerExtension(disco.InfoResult)
			registerExtension(disco.Items)
			registerExtension(disco.ItemsResult)
			
			def create(xml:Node):IQ =
			{				
				(xml \ "@type").text match
				{
					// FIXME, use the enum values (attribute kind) instead of kindName, getting compilation error even with implicict cast
					case Get.kindName => Get(xml) 
					case Set.kindName => Set(xml)
					case Result.kindName => Result(xml) 
					case Error.kindName => Error(xml)
					case _ => throw new Exception("unknown iq stanza") // TODO, give a more detailed error message here
				}
			}
		}
		
	}
}
