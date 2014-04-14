package com.mishlabs.xmpp
package protocol

import scala.xml._

object Handshake
{
    val tag = "handshake"

    def apply(key:String):Handshake =
    {
        return new Handshake(<handshake>{ key }</handshake>)
    }

    def apply(xml:Node):Handshake = new Handshake(xml)

    def apply():Handshake = apply(<handshake/>)
}

class Handshake(xml:Node) extends XmlWrapper(xml) with Packet
{
    val key:String =  this.xml.text
}