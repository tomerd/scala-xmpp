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
			def create(xml:Node):Option[IQ] =
			{
				xml match
				{
					// FIXME, handle other cases here
					case root @ <iq>{ extensions @ _* }</iq> if (root \ "@type").text == IQTypeEnumeration.Get.toString => Some(Get(root))
					case root @ <iq>{ extensions @ _* }</iq> if (root \ "@type").text == IQTypeEnumeration.Set.toString => Some(Set(root))
					case root @ <iq>{ extensions @ _* }</iq> if (root \ "@type").text == IQTypeEnumeration.Result.toString => Some(Result(root))
					case root @ <iq>{ extensions @ _* }</iq> if (root \ "@type").text == IQTypeEnumeration.Error.toString => Some(Error(root))
					case _ => None
				}				
			}
		}
		
	}
}
