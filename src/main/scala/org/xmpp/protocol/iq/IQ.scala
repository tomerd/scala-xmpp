package org.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		protected[xmpp] object IQ
		{
			val tag = "iq"
									
			def build(stanzaType:IQTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], extension:Option[Extension]):Node = Stanza.build(tag, stanzaType.toString, id, to, from, extension)
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]):Node = Stanza.error(tag, id, to, from, condition, description)
		}
		
		abstract class IQ(xml:Node, val stanzaType:IQTypeEnumeration.Value) extends Stanza(xml)
		{
		}
				
		object IQTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val Get = Value("get")
			val Set = Value("set")
			val Result = Value("result")
			val Error = Value("error")
		}
		
	}
}
