package com.mishlabs.xmpp
package protocol
package extensions
package roster

import scala.collection._
import scala.xml._

import Protocol._
		
object RosterResult
{
    def apply(items:Seq[RosterItem]):RosterResult = apply(Builder.build(items))

    def apply(xml:Node):RosterResult = new RosterResult(xml)
}

class RosterResult(xml:Node) extends Query(xml)
{
    val items:Seq[RosterItem] = (this.xml \ "item").map( node => RosterItem(node) )
}