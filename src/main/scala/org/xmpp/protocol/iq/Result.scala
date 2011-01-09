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
			val kind = IQTypeEnumeration.Result
			val kindName = kind.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]=None):Result = apply(build(id, to, from, extensions))
			
			def apply(xml:Node) = new Result(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]=None):Node = IQ.build(kind, id, to, from, extensions)
		}
		
		class Result(xml:Node) extends IQ(xml, Result.kind)
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
