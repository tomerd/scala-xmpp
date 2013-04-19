package com.mishlabs.xmpp
package protocol

import scala.xml._

import Protocol._

abstract class Extension(xml:Node) extends XmlWrapper(xml)
{
    val tag:String =  this.xml.label
    val namespace:Option[String] = this.xml.scope.uri
}