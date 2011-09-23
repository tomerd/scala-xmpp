scala-xmpp is a scala based xmpp framework providing implementations of core xmpp stanza encoding/decoding and a framework for building external components and clients. 

scala-xmpp is similar to ejabbered's exmpp and openfire's tinder/whack libraries but is a native scala implementation. it utilizes scala's concurrency features, java nio sockets (via netty and naggati) and brings a fresh approach on stanza encoding/decoding. 

simple external component implementation
----------------------------------------

import org.simbit.xmpp.component.XmppComponent
		
import org.simbit.xmpp.protocol.extensions._
		
import org.simbit.xmpp.protocol._
import org.simbit.xmpp.protocol.message._

import org.simbit.xmpp.protocol.Protocol._

class Echo extends XmppComponent 
{
	override val identities = List(disco.Identity("component", "c2s", "echo server"))
	override val extensionsBuilders = Nil
	
	override def handleMessage(message:Message)			
	{
		send(Chat(message.from, message.to, message.subject, message.body))
	}
}

TODO: add helpful project information here

***************************************************************************************
***************************************************************************************

THIS PROJECT IS STILL IN INCUBATION

if you are interested in contributing, please touch base

***************************************************************************************
***************************************************************************************

see license.txt for license info


