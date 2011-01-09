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
					// FIXME, use enum values instead of hard coded string here (getting compilation error. even with implicict cast)
					case "get" => Get(xml) // IQTypeEnumeration.Get
					case "set" => Set(xml) // IQTypeEnumeration.Set
					case "result" => Result(xml) // IQTypeEnumeration.Result
					case "error" => Error(xml) // IQTypeEnumeration.Error
					case _ => throw new Exception("unknown iq stanza") // TODO, give a more detailed error message here
				}				
			}
		}
		
	}
}
