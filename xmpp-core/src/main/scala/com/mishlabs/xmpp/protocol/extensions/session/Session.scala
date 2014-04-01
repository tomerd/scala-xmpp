package com.mishlabs.xmpp
package protocol
package extensions
package session

import scala.xml._

object Session
{
    val tag = "session"

    def apply():Session = apply(<session xmlns={ Builder.namespace }/>)

    def apply(xml:Node):Session = new Session(xml)
}

class Session(xml:Node) extends Extension(xml)
