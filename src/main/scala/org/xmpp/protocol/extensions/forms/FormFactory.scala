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
		
		object FormFactory extends ExtensionBuilder[Form]
		{
			val name = X.name
			val namespace = "jabber:x:data"
			
			def apply(xml:Node):Form = 
			{
				require("form" == xml.label)
				
				(xml \ "@type").text match
				{ 
					// FIXME, use the enum values (attribute formType) instead of formTypeName, getting compilation error even with implicict cast
					case Basic.formTypeName => Basic(xml)
					case Submit.formTypeName => Submit(xml) 
					case Result.formTypeName => Result(xml) 
					case Cancel.formTypeName => Cancel(xml)
					case _ => throw new Exception("unknown form extention") // TODO, give a more detailed error message here
				}
			}
		}
		
	}
}
