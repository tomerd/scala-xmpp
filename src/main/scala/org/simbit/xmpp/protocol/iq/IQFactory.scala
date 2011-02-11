package org.simbit.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object IQFactory //extends StanzaFactory[IQ]
		{
			def create(xml:Node):IQ =
			{
				require("iq" == xml.label)
				
				(xml \ "@type").text match
				{
					// FIXME, use the enum values (attribute iqType) instead of iqTypeName, getting compilation error even with implicict cast
					case Get.iqTypeName => Get(xml) 
					case Set.iqTypeName => Set(xml)
					case Result.iqTypeName => Result(xml) 
					case Error.iqTypeName => Error(xml)
					case _ => throw new Exception("unknown iq stanza") // TODO, give a more detailed error message here
				}
			}
		}
		
	}
}
