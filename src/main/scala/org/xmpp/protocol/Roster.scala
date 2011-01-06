package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._

		final object RosterQuery
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID]):RosterQuery = 
			{				
				val xml = Stanza.build(IQ.TAG, id, to, from, Some(IQTypeEnumeration.Get.toString), Some(Extension("query", "jabber:iq:roster")))
				return new RosterQuery(xml)	
			}
		}
		
		final class RosterQuery(xml:Node) extends IQ(xml)
		{
			def result(items:Seq[RosterItem]):RosterResult = RosterResult(this.id, this.from, this.to, items)
		}
		
		final object RosterResult
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID], items:Seq[RosterItem]):RosterResult = 
			{
				val xml = Stanza.build(IQ.TAG, id, to, from, Some(IQTypeEnumeration.Result.toString), Some(Extension("query", "jabber:iq:roster", Some(items))))
				return new RosterResult(xml)
			}
		}
			
		final class RosterResult(xml:Node) extends IQ(xml)
		{
		}
		
		final object RosterItem
		{
			val TAG = "item"
			
			def apply(jid:Option[JID]):RosterItem = apply(jid, None, None, None, None)
					
			def apply(jid:Option[JID], name:Option[String]):RosterItem = apply(jid, name, None, None, None)
			
			def apply(jid:Option[JID], name:Option[String], subscrption:Option[RosterItemSubscription.Value], ask:Option[RosterItemAsk.Value], groups:Option[Seq[String]]):RosterItem =
			{
				val children = mutable.ListBuffer[Node]()
				if (!groups.isEmpty) groups.get.foreach( group => children += <groups>{ group }</groups> )
				var metadata:MetaData = Null
				if (!jid.isEmpty) metadata = metadata.append(new UnprefixedAttribute("jid", Text(jid.get), Null))
				if (!name.isEmpty) metadata = metadata.append(new UnprefixedAttribute("name", Text(name.get), Null))
				if (!subscrption.isEmpty) metadata = metadata.append(new UnprefixedAttribute("subscrption", Text(subscrption.get.toString), Null))
				if (!ask.isEmpty) metadata = metadata.append(new UnprefixedAttribute("ask", Text(ask.get.toString), Null))
				return new RosterItem(Elem(null, TAG, metadata, TopScope, children:_*))
			}
		}
		
		final class RosterItem(xml:Node) extends XmlWrapper(xml)
		{
			parse
			
			// getters
			private var _jid:Option[JID] = None
			private def jid:Option[JID] = _jid
			
			private var _name:Option[String] = None
			private def name:Option[String] = _name
			
			private var _subscrption:Option[RosterItemSubscription.Value] = None
			private def subscrption:Option[RosterItemSubscription.Value] = _subscrption
			
			private var _ask:Option[RosterItemAsk.Value] = None
			private def ask:Option[RosterItemAsk.Value] = _ask			
			
			private var _groups:Option[Seq[String]] = None
			private def groups:Option[Seq[String]] = _groups
			
			private def parse
			{
				val jid = (this.xml \ "@jid").text
				_jid = if (jid.isEmpty) None else Some(JID(jid))
				
				val name = (this.xml \ "@name").text
				_name = if (name.isEmpty) None else Some(name)
				
				val subscrption = (this.xml \ "@subscrption").text
				_subscrption = if (subscrption.isEmpty) None else Some(RosterItemSubscription.withName(subscrption))
				
				val ask = (this.xml \ "@ask").text
				_ask = if (ask.isEmpty) None else Some(RosterItemAsk.withName(ask))
				
				val groupNodes = (this.xml \ "group")
				_groups = if (0 == groupNodes.length) None else Some( groupNodes.map( node => node.label ) )
			}
		}
		
		final object RosterItemSubscription extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val None = Value("none")
			val To = Value("to")
			val From = Value("from")
			val Both = Value("both")
			val Remove = Value("remove")			
		}
		
		final object RosterItemAsk extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val Subscribe = Value("subscribe")
			val Unsubscribe = Value("unsubscribe")
		}		
	}
}
