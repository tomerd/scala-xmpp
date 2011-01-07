package org.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		object Get
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Get = 
			{				
				val xml = Stanza.build(IQ.TAG, id, to, from, IQTypeEnumeration.Get.toString, extensions)
				return new Get(xml)	
			}
			
			def apply(xml:Node):Get = new Get(xml)
		}
		
		class Get(xml:Node) extends IQ(xml)
		{
			def result(extensions:Option[Seq[Extension]]):Result = Result(this.id, this.from, this.to, extensions)
		}
		
	}
}
