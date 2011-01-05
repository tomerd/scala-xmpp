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
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[IQTypeEnumeration.Value]):IQ =
			{
				val children = mutable.ListBuffer[Node]() // TODO, remove? not required
				var attributes:MetaData = Null
				if (!id.isEmpty) attributes = attributes.append(new UnprefixedAttribute("id", Text(id.get), Null))
				if (!to.isEmpty) attributes = attributes.append(new UnprefixedAttribute("to", Text(to.get), Null))
				if (!from.isEmpty) attributes = attributes.append(new UnprefixedAttribute("from", Text(from.get), Null))
				if (!kind.isEmpty) attributes = attributes.append(new UnprefixedAttribute("type", Text(kind.get.toString), Null))				
				return new IQ(Elem(null, TAG, attributes, TopScope, children:_*))
			}	
						
			def error(id:Option[String], to:Option[JID], from:Option[JID], errorCondition:ErrorCondition.Value, errorDescription:Option[String]=None):IQ =
			{
				// TODO: test this
				var attributes:MetaData = new UnprefixedAttribute("type", Text(IQTypeEnumeration.Error.toString), Null)
				if (!id.isEmpty) attributes = attributes.append(new UnprefixedAttribute("id", Text(id.get), Null))
				if (!to.isEmpty) attributes = attributes.append(new UnprefixedAttribute("to", Text(to.get), Null))
				if (!from.isEmpty) attributes = attributes.append(new UnprefixedAttribute("from", Text(from.get), Null))		
				return new IQ(Elem(null, TAG, attributes, TopScope, Error(errorCondition, errorDescription)))				
			}			
		}		
		
		class IQ(xml:Node) extends Stanza[IQ](xml)
		{
			val TypeEnumeration = IQTypeEnumeration
						
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
