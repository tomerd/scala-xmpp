package com.mishlabs.xmpp
package protocol
package extensions
package forms

import scala.collection._
import scala.xml._

import Protocol._
		
private[xmpp] object Builder extends ExtensionBuilder[Form]
{
    val tag = X.tag
    val namespace = "jabber:x:data"

    def apply(xml:Node):Form =
    {
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