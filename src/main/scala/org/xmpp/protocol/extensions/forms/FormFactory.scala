package org.xmpp
{
	package protocol.extensions.forms
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.message._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object FormFactory extends ExtendedStanzaBuilder[Form]
		{
			val stanzaType = Container.stanzaTypeName
			val name = X.name
			val namespace = "jabber:x:data"
				
			def apply(xml:Node):Form = 
			{
				require (1 == xml.child.length)
				require("form" == xml.child(0).label)
				
				(xml.child \ "@type").text match
				{ 
					// FIXME, use the enum values (attribute formType) instead of formTypeName, getting compilation error even with implicict cast
					case Basic.formTypeName => Basic(xml.child(0)) 
					case Submit.formTypeName => Submit(xml.child(0)) 
					case Result.formTypeName => Result(xml.child(0)) 
					case Cancel.formTypeName => Cancel(xml.child(0))
					case _ => throw new Exception("unknown form extention") // TODO, give a more detailed error message here
				}
			}
		}
		
	}
}
