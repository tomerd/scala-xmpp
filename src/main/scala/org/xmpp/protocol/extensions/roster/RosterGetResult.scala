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
		
		object RosterGetResult extends ExtendedStanzaBuilder[RosterGetResult]
		{
			val stanzaType = Result.stanzaTypeName
			val name = Query.name
			val namespace = RosterGet.namespace
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], items:Seq[RosterItem]):RosterGetResult = 
			{
				val xml = Get.build(id, to, from, Query(namespace, items))
				return apply(xml)
			}
			
			def apply(xml:Node):RosterGetResult = new RosterGetResult(xml)
		}
		
		class RosterGetResult(xml:Node) extends Result(xml)
		{
			val items:Seq[RosterItem] = (this.xml \ "item").map( node => RosterItem(node) )
		}
	}
}
