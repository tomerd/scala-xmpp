package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._
		
		final object IQ
		{
			val TAG = "iq"
			
			def apply():IQ = apply(None, None, None, Some(IQTypeEnumeration.Get), None)
			
			def apply(kind:Option[IQTypeEnumeration.Value]):IQ = apply(None, None, None, kind, None)
			
			def apply(kind:Option[IQTypeEnumeration.Value], extensions:Option[Seq[Extension]]):IQ = apply(None, None, None, kind, extensions)
						
			def apply(id:Option[String], kind:Option[IQTypeEnumeration.Value], extensions:Option[Seq[Extension]]=None):IQ = apply(id, None, None, kind, extensions)
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[IQTypeEnumeration.Value], extensions:Option[Seq[Extension]]):IQ =
			{
				val xml = Stanza.build(TAG, id, to, from, kind, extensions)
				return new IQ(xml)
			}	
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]=None):IQ =
			{
				val xml = Stanza.error(TAG, id, to, from, condition, description)
				return new IQ(xml)
			}
		}
		
		class IQ(xml:Node) extends Stanza(xml)
		{
			val TypeEnumeration = IQTypeEnumeration
						
			final def result:IQ = IQ(this.id, this.to, this.from, Some(IQTypeEnumeration.Result), None)
			
			final def error(condition:ErrorCondition.Value, description:Option[String]=None):IQ = IQ.error(this.id, this.from, this.to, condition, description)
		}
				
		final object IQTypeEnumeration extends Enumeration
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
