package org.simbit.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
		
		object Result
		{
			val iqType = IQTypeEnumeration.Result
			val iqTypeName = iqType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(id:Option[String], to:JID, from:JID, extension:Option[Extension]=None):Result = apply(build(id, to, from, extension))
			
			def apply(xml:Node):Result = new Result(xml)
			
			def build(id:Option[String], to:JID, from:JID, extension:Option[Extension]=None):Node = IQ.build(iqType, id, to, from, extension)
			
			def unapply(result:Result):Option[(Option[String], JID, JID, Option[Extension])] = Some(result.id, result.to, result.from, result.extension)
		}
		
		class Result(xml:Node) extends IQ(xml, Result.iqType)
		{			
		}
		
	}
}
