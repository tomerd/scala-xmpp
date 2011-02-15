package org.simbit.xmpp
{
	package protocol.presence
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
			
		object DoNotDisturb
		{
			def apply(id:Option[String], to:JID, from:JID, status:Option[String], priority:Option[Int], extensions:Option[Seq[Extension]]):DoNotDisturb =
			{
				val xml = Available.build(id, to, from, Some(Show.DND), status, priority, extensions)
				return apply(xml)
			}
			
			def apply(xml:Node):DoNotDisturb = new DoNotDisturb(xml)
		}
		
		class DoNotDisturb(xml:Node) extends Available(xml, Some(Show.DND))
		{
		}
	}
}