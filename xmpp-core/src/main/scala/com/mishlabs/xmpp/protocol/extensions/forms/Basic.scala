package com.mishlabs.xmpp
package protocol
package extensions
package forms

import scala.collection._
import scala.xml._

import fields._

import Protocol._

object Basic
{
    val formType = FormTypeEnumeration.Form
    val formTypeName = formType.toString

    def apply(fields:Seq[Field]):Basic = apply(None, None, fields)

    def apply(title:Option[String], fields:Seq[Field]):Basic = apply(title, None, fields)

    def apply(title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Basic = apply(build(title, instructions, fields))

    def apply(xml:Node):Basic = new Basic(xml)

    def build(title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
    {
        Form.build(Basic.formType, title, instructions, fields)
    }
}

class Basic(xml:Node) extends Form(xml, Basic.formType)
{
}