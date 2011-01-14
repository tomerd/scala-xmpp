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
