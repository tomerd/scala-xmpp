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
			val iqTypeName = iqType.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Get = apply(build(id, to, from, extension)) 
			
			def apply(xml:Node):Get = new Get(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Node = IQ.build(iqType, id, to, from, extension)
		}
		
		class Get(xml:Node) extends IQ(xml, Get.iqType)
		{
			def result(extension:Option[Extension]):Result = Result(this.id, this.from, this.to, extension)
		}
		
	}
}
