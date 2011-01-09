package org.xmpp
{
	package protocol.roster
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.iq._
		
		import org.xmpp.protocol.Protocol._
		
		object RosterGetResult extends ExtendedStanzaBuilder[RosterGetResult]
		{
			val kind = Result.kindName
			val name = Query.name
			val namespace = RosterGet.namespace
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], items:Seq[Item]):RosterGetResult = 
			{
				val xml = Get.build(id, to, from, Query(namespace, items))
				return apply(xml)
			}
			
			def apply(xml:Node):RosterGetResult = new RosterGetResult(xml)
		}
		
		class RosterGetResult(xml:Node) extends Result(xml)
		{
			// getters
			/*
			private var _items:Option[Seq[RosterItem]] = None
			private def items:Option[Seq[RosterItem]] = _items
			
			override protected def parse
			{
				super.parse
			
				val itemsNodes = (this.xml \ "item")
				_items = if (0 == itemsNodes.length) None else Some(itemsNodes.map( node => new RosterItem(node) ))
			}
			*/
		}
	}
}
