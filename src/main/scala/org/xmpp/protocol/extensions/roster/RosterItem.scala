package org.xmpp
{
	package protocol.extensions.roster
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
		
		object RosterItem
		{
			def apply(jid:Option[JID]):RosterItem = apply(build(jid, None, None, None, None))
					
			def apply(jid:Option[JID], name:Option[String]):RosterItem = apply(build(jid, name, None, None, None))
			
			def apply(jid:Option[JID], name:Option[String], subscription:Option[ItemSubscription.Value], ask:Option[ItemAsk.Value], groups:Option[Seq[String]]):Node = apply(build(jid, name, subscription, ask, groups))
			
			def apply(xml:Node) = new RosterItem(xml)
			
			def build(jid:Option[JID], name:Option[String], subscrption:Option[ItemSubscription.Value], ask:Option[ItemAsk.Value], groups:Option[Seq[String]]):Node =
			{
				val children = mutable.ListBuffer[Node]()
				if (!groups.isEmpty) groups.get.foreach( group => children += <groups>{ group }</groups> )
				var metadata:MetaData = Null
				if (!jid.isEmpty) metadata = metadata.append(new UnprefixedAttribute("jid", Text(jid.get), Null))
				if (!name.isEmpty) metadata = metadata.append(new UnprefixedAttribute("name", Text(name.get), Null))
				if (!subscrption.isEmpty) metadata = metadata.append(new UnprefixedAttribute("subscrption", Text(subscrption.get.toString), Null))
				if (!ask.isEmpty) metadata = metadata.append(new UnprefixedAttribute("ask", Text(ask.get.toString), Null))
				return Item.build(metadata, children)
			}
		}
		
		class RosterItem(xml:Node) extends Item(xml)
		{			
			// getters
			/*
			private var _jid:Option[JID] = None
			private def jid:Option[JID] = _jid
			
			private var _name:Option[String] = None
			private def name:Option[String] = _name
			
			private var _subscrption:Option[ItemSubscription.Value] = None
			private def subscrption:Option[ItemSubscription.Value] = _subscrption
			
			private var _ask:Option[ItemAsk.Value] = None
			private def ask:Option[ItemAsk.Value] = _ask			
			
			private var _groups:Option[Seq[String]] = None
			private def groups:Option[Seq[String]] = _groups
			
			override protected def parse
			{
				super.parse
				
				val jid = (this.xml \ "@jid").text
				_jid = if (jid.isEmpty) None else Some(JID(jid))
				
				val name = (this.xml \ "@name").text
				_name = if (name.isEmpty) None else Some(name)
				
				val subscrption = (this.xml \ "@subscrption").text
				_subscrption = if (subscrption.isEmpty) None else Some(ItemSubscription.withName(subscrption))
				
				val ask = (this.xml \ "@ask").text
				_ask = if (ask.isEmpty) None else Some(ItemAsk.withName(ask))
				
				val groupNodes = (this.xml \ "group")
				_groups = if (0 == groupNodes.length) None else Some( groupNodes.map( node => node.label ) )
			}
			*/
			
			private def jid:Option[JID] = 
			{
				val jid = (this.xml \ "@jid").text
				if (jid.isEmpty) None else Some(JID(jid))
			}
			
			private def name:Option[String] = 
			{
				val name = (this.xml \ "@name").text
				if (name.isEmpty) None else Some(name)
			}
			
			private def subscrption:Option[ItemSubscription.Value] = 
			{
				val subscrption = (this.xml \ "@subscrption").text
				if (subscrption.isEmpty) None else Some(ItemSubscription.withName(subscrption))
			}
			
			private def ask:Option[ItemAsk.Value] = 
			{
				val ask = (this.xml \ "@ask").text
				if (ask.isEmpty) None else Some(ItemAsk.withName(ask))
			}
			
			private def groups:Option[Seq[String]] = 
			{
				val groupNodes = (this.xml \ "group")
				if (0 == groupNodes.length) None else Some( groupNodes.map( node => node.label ) )				
			}
		}
		
		object ItemSubscription extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val None = Value("none")
			val To = Value("to")
			val From = Value("from")
			val Both = Value("both")
			val Remove = Value("remove")
		}
		
		object ItemAsk extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val Subscribe = Value("subscribe")
			val Unsubscribe = Value("unsubscribe")
		}		
	}
}
