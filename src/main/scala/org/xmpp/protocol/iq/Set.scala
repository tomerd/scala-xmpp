package org.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Set
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Set = 
			{				
				val xml = IQ.build(IQTypeEnumeration.Set, id, to, from, extensions)
				return apply(xml)	
			}
			
			def apply(xml:Node):Set = new Set(xml)
		}
		
		class Set(xml:Node) extends IQ(xml, IQTypeEnumeration.Set)
		{
			def result(extensions:Option[Seq[Extension]]):Result = Result(this.id, this.from, this.to, extensions)
		}
		
	}
}
