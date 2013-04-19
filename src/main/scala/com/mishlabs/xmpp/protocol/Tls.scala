package com.mishlabs.xmpp
package protocol

import scala.xml.{Node, XML}

object Tls
{
    val namespace = "urn:ietf:params:xml:ns:xmpp-tls"
}

object StartTls
{
    val tag = "starttls"

    def apply():StartTls = apply(<starttls xmlns={ Tls.namespace }/>)

    def apply(xml:Node):StartTls = new StartTls(xml)
}

class StartTls(xml:Node) extends XmlWrapper(xml) with Packet
{
}

object TlsProceed
{
    val tag = "proceed"

    def apply():TlsProceed = new TlsProceed(<proceed xmlns={ Tls.namespace }/>)

    def apply(xml:Node):TlsProceed = new TlsProceed(xml)
}

class TlsProceed(xml:Node) extends XmlWrapper(xml) with Packet
{
}

object TlsFailure
{
    val tag = "failure"

    def apply():TlsFailure = new TlsFailure(<failure xmlns={ Tls.namespace }/>)

    def apply(xml:Node):TlsFailure = new TlsFailure(xml)
}

class TlsFailure(xml:Node) extends XmlWrapper(xml) with Packet
{
}




