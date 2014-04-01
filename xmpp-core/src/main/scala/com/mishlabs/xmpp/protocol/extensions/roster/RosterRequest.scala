package com.mishlabs.xmpp
package protocol
package extensions
package roster

import scala.collection._
import scala.xml._

import Protocol._
		
object RosterRequest
{
    def apply(xml:Node):RosterRequest = new RosterRequest(xml)
}

class RosterRequest(xml:Node) extends Query(xml)
{
    def result(items:Seq[RosterItem]):RosterResult = RosterResult(items)
}