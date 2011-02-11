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
		
		object RosterResult
		{
			def apply(items:Seq[RosterItem]):RosterResult = apply(Builder.build(items))
			
			def apply(xml:Node):RosterResult = new RosterResult(xml)
		}
		
		class RosterResult(xml:Node) extends Query(xml)
		{
			val items:Seq[RosterItem] = (this.xml \ "item").map( node => RosterItem(node) )
		}
	}
}
