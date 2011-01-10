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
			val stanzaType = IQTypeEnumeration.Result
			val stanzaTypeName = stanzaType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Result = apply(build(id, to, from, extension))
			
			def apply(xml:Node):Result = new Result(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Node = IQ.build(stanzaType, id, to, from, extension)
		}
		
		class Result(xml:Node) extends IQ(xml, Result.stanzaType)
		{			
		}
		
	}
}
