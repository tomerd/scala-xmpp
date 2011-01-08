package org.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Result
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Result = 
			{
				val xml = IQ.build(IQTypeEnumeration.Result, id, to, from, extensions)
				return apply(xml)
			}
			
			def apply(xml:Node) = new Result(xml)
		}
		
		class Result(xml:Node) extends IQ(xml, IQTypeEnumeration.Result)
		{
			/*
			// getters
			private var _items:Option[Seq[Item]] = None
			private def items:Option[Seq[Item]] = _items
			
			override protected def parse
			{
				super.parse
			
				val itemsNodes = (this.xml \ "item")
				_items = if (0 == itemsNodes.length) None else Some(itemsNodes.map( node => new IQItem(node) ))
			}
			*/
		}
		
	}
}
