package org.xmpp
{
	package protocol.extensions.roster
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object Roster extends
		{
			def apply(xml:Node):Roster = new Roster(xml)
		}
		
		class Roster(xml:Node) extends Query(xml)
		{
			def result(items:Seq[RosterItem]):RosterResult = RosterResult(items)
		}
		
	}
}
