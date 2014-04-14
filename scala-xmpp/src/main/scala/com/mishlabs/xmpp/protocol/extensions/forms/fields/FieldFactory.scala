package com.mishlabs.xmpp
package protocol
package extensions
package forms
package fields

import scala.collection._
import scala.xml._

import Protocol._

protected[forms] object FieldFactory
{
    def create(xml:Node):Field =
    {
        require("field" == xml.label)

        (xml \ "@type").text match
        {
            // FIXME, use the enum values (attribute formType) instead of fieldTypeName, getting compilation error even with implicict cast
            case Bool.fieldTypeName => Bool(xml)
            case Fixed.fieldTypeName => Fixed(xml)
            case Hidden.fieldTypeName => Hidden(xml)
            case MultiJid.fieldTypeName => MultiJid(xml)
            case SingleJid.fieldTypeName => SingleJid(xml)
            case MultiList.fieldTypeName => MultiList(xml)
            case SingleList.fieldTypeName => SingleList(xml)
            case MultiText.fieldTypeName => MultiText(xml)
            case SingleText.fieldTypeName => SingleText(xml)
            case PrivateText.fieldTypeName => PrivateText(xml)
            case _ => throw new Exception("unknown field type") // TODO, give a more detailed error message here
        }

        /*
        xml match
        {
            case field @ <field/> if !(field \ "value").isEmpty => SimpleField(xml)
            case field @ <field/> if !(field \ "option").isEmpty => OptionsField(xml)
            case _ => throw new Exception("unknown field")
        }
        */
    }
}
