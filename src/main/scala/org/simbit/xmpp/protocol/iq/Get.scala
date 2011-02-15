package org.simbit.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.Protocol._
		
		object Get
		{
			val iqType = IQTypeEnumeration.Get
			val iqTypeName = iqType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reason
			
			def apply(id:Option[String], to:JID, from:JID, extension:Option[Extension]=None):Get = apply(build(id, to, from, extension)) 
			
			def apply(xml:Node):Get = new Get(xml)
			
			def build(id:Option[String], to:JID, from:JID, extension:Option[Extension]=None):Node = IQ.build(iqType, id, to, from, extension)
			
			def unapply(get:Get):Option[(Option[String], JID, JID, Option[Extension])] = Some(get.id, get.to, get.from, get.extension)
		}
		
		class Get(xml:Node) extends IQ(xml, Get.iqType)
		{
			def result(extension:Option[Extension]=None):Result = Result(this.id, this.from, this.to, extension)
		}
		
	}
}
