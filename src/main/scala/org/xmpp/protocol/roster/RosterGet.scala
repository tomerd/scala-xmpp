package org.xmpp
{
	package protocol.roster
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		
		import org.xmpp.protocol.Protocol._
		
		object RosterGet extends ExtendedStanzaBuilder[RosterGet]
		{			
			val kind = Get.kindName
			val name = Query.name
			val namespace = "jabber:iq:roster"
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):RosterGet = 
			{
				val xml = Get.build(id, to, from, Query(namespace))
				return apply(xml)
			}
			
			def apply(xml:Node):RosterGet = new RosterGet(xml)
		}
		
		class RosterGet(xml:Node) extends Get(xml)
		{
			def result(items:Seq[Item]):RosterGetResult = RosterGetResult(this.id, this.from, this.to, items)
		}
		
	}
}
