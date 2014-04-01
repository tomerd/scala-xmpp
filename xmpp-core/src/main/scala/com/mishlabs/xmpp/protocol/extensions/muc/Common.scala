package com.mishlabs.xmpp
package protocol
package extensions
package muc

object Affiliation extends Enumeration
{
    type value = Value

    val Unknown = Value("unknown") // internal use
    val Owner = Value("owner")
    val Admin = Value("admin")
    val Member = Value("member")
}

object Role extends Enumeration
{
    type value = Value

    val Unknown = Value("unknown") // internal use
    val Moderator = Value("moderator")
    val Participant = Value("participant")
    val Visitor = Value("visitor")
    val None = Value("none")
}
