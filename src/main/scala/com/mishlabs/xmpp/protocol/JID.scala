package com.mishlabs.xmpp
package protocol

object JID
{
    def apply(string:String):JID =
    {
        val array1 = string.split("@")
        val array2 = if (2 == array1.length) array1(1).split("/") else string.split("/")

        val node = if (2 == array1.length) array1(0) else null
        val resource = if (2 == array2.length) array2(1) else null
        val domain = if (2 == array2.length) array2(0) else if (2 == array1.length) array1(1) else string

        JID(node, domain, resource)
    }
}

case class JID(node:String, domain:String, resource:String)
{
    def validate()
    {
        // TODO: implement this according to the XMPP spec
    }

    private val hasNode = (null != this.node && !this.node.isEmpty)

    private val hasResource = (null != this.resource && !this.resource.isEmpty)

    val bare:JID = if (this.hasResource) JID(this.node, this.domain, null) else this

    private val display =
    {
        val builder = new StringBuilder()
        if (this.hasNode) builder.append(this.node).append("@")
        builder.append(this.domain)
        if (this.hasResource) builder.append("/").append(this.resource)
        builder.toString()
    }

    def isEmpty = !hasNode && !hasResource && domain.isEmpty

    override def toString:String = display

    override def equals(other:Any) = other match
    {
        case string:String => string == this.toString
        case jid:JID if (null != jid) => jid.toString == this.toString
        case obj:Object if (null != obj) => obj.toString == this.toString
        case _ => false
    }
}