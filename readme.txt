scala-xmpp is a scala based xmpp framework providing implementations of core xmpp stanza encoding/decoding and a framework for building external components. scala-xmpp can be used to build both server and client xmpp applications.  

scala-xmpp is similar to ejabbered's exmpp and openfire's tinder/whack libraries but is wriiten is native scala utilizing scala's concurrency features and takes a different approach on stanza encoding/decoding. 

*****************************
THIS PROJECT IS IN INCUBATION
*****************************

TODO: add helpful project information here


simple component implementation
-------------------------------

package org.simbit.element
{
	package services.echo
	{		
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
		
	}
}





see license.txt for license info


