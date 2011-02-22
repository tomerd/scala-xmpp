package org.simbit.xmpp
{
	package protocol.extensions.roster
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.iq._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object RosterRequest extends
		{
			def apply(xml:Node):RosterRequest = new RosterRequest(xml)
		}
		
		class RosterRequest(xml:Node) extends Query(xml)
		{
			def result(items:Seq[RosterItem]):RosterResult = RosterResult(items)
		}
		
	}
}
