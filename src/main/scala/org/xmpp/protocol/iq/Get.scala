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
			val kind = IQTypeEnumeration.Get
			val kindName = kind.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]=None):Get = apply(build(id, to, from, extensions)) 
			
			def apply(xml:Node):Get = new Get(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]=None):Node = IQ.build(kind, id, to, from, extensions)
		}
		
		class Get(xml:Node) extends IQ(xml, Get.kind)
		{
			def result(extensions:Option[Seq[Extension]]):Result = Result(this.id, this.from, this.to, extensions)
		}
		
	}
}
