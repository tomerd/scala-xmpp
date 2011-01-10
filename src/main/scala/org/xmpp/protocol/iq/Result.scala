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
			
			def apply(xml:Node) = new Result(xml)
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Result = apply(build(id, to, from, extension))
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Node = IQ.build(kind, id, to, from, extension)
		}
		
		class Result(xml:Node) extends IQ(xml, Result.kind)
		{			
		}
		
	}
}
