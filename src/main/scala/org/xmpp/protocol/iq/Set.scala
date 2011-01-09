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
			val kind = IQTypeEnumeration.Set
			val kindName = kind.toString // FIXME, this should be done automatically via implicit def, but does not work for enum values for some reson
			
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Set = apply(build(id, to, from, extension)) 
			
			def apply(xml:Node):Set = new Set(xml)
			
			def build(id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]=None):Node = IQ.build(kind, id, to, from, extension)
		}
		
		class Set(xml:Node) extends IQ(xml, Set.kind)
		{
			def result(extension:Option[Extension]):Result = Result(this.id, this.from, this.to, extension)
		}
		
	}
}
