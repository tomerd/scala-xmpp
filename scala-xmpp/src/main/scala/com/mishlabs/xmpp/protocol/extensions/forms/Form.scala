package com.mishlabs.xmpp
package protocol
package extensions
package forms

import scala.collection._
import scala.xml._

import fields._

import Protocol._

protected object Form
{
    //def build( formType:FormTypeEnumeration.Value, title:Option[String], instructions:Option[Seq[String]], fields:Seq[Field]):Node =
    def build(formType:FormTypeEnumeration.Value, title:Option[String], instructions:Option[Seq[String]], children:Seq[Node]):Node =
    {
        val kids = mutable.ListBuffer[Node]()
        if (!title.isEmpty) kids += <title>{ title }</title>
        if (!instructions.isEmpty) instructions.get.foreach( instruction => kids += <instructions>{ instruction }</instructions>)
        kids ++= children
        var metadata:MetaData = new UnprefixedAttribute("type", Text(formType.toString), Null)
        return Builder.build(metadata, children)
    }
}

abstract class Form(xml:Node, val formType:FormTypeEnumeration.Value) extends X(xml)
{
    val title:Option[String] = (this.xml \ "title").text

    val instructions:Option[Seq[String]] = (this.xml \ "instructions").map( node => node.text )

    val fields:Seq[Field] = (this.xml \ "fields").map( node => FieldFactory.create(node) )
}

protected object FormTypeEnumeration extends Enumeration
{
    type value = Value

    val Unknown = Value("unknown") // internal use
    val Form = Value("form")
    val Submit = Value("submit")
    val Result = Value("result")
    val Cancel = Value("cancel")
}
