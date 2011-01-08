package org.xmpp
{
	package protocol.iq
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.Protocol._
		
		protected object IQ
		{
			val TAG = "iq"
									
			def build(kind:IQTypeEnumeration.Value, id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):Node = Stanza.build(TAG, kind.toString, id, to, from, extensions)
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]):Node = Stanza.error(TAG, id, to, from, condition, description)
		}
		
		abstract class IQ(xml:Node, val kind:IQTypeEnumeration.Value) extends Stanza(xml)
		{
			//val TypeEnumeration = IQTypeEnumeration
						
			//def result:IQResult = new IQResult(this.id, this.to, this.from, Some(IQTypeEnumeration.Result), None)
			
			//def error(condition:ErrorCondition.Value, description:Option[String]=None):IQ = IQ.error(this.id, this.from, this.to, condition, description)
		}
				
		protected object IQTypeEnumeration extends Enumeration
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
