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
			
			def apply(kind:Option[IQTypeEnumeration.Value], extension:Option[Extension]):IQ = apply(None, None, None, kind, extension)
						
			def apply(id:Option[String], kind:Option[IQTypeEnumeration.Value], extension:Option[Extension]=None):IQ = apply(id, None, None, kind, extension)
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[IQTypeEnumeration.Value], extension:Option[Extension]):IQ =
			{
				val children = mutable.ListBuffer[Node]() 
				if (!extension.isEmpty) children += extension.get
				var metadata:MetaData = Null
				if (!id.isEmpty) metadata = metadata.append(new UnprefixedAttribute("id", Text(id.get), Null))
				if (!to.isEmpty) metadata = metadata.append(new UnprefixedAttribute("to", Text(to.get), Null))
				if (!from.isEmpty) metadata = metadata.append(new UnprefixedAttribute("from", Text(from.get), Null))
				if (!kind.isEmpty) metadata = metadata.append(new UnprefixedAttribute("type", Text(kind.get.toString), Null))
				return new IQ(Elem(null, TAG, metadata, TopScope, children:_*))
			}	
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], errorCondition:ErrorCondition.Value, errorDescription:Option[String]=None):IQ =
			{
				var metadata:MetaData = new UnprefixedAttribute("type", Text(IQTypeEnumeration.Error.toString), Null)
				if (!id.isEmpty) metadata = metadata.append(new UnprefixedAttribute("id", Text(id.get), Null))
				if (!to.isEmpty) metadata = metadata.append(new UnprefixedAttribute("to", Text(to.get), Null))
				if (!from.isEmpty) metadata = metadata.append(new UnprefixedAttribute("from", Text(from.get), Null))		
				return new IQ(Elem(null, TAG, metadata, TopScope, Error(errorCondition, errorDescription)))				
			}			
		}		
		
		class IQ(xml:Node) extends Stanza[IQ](xml)
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
