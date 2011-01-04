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
				
			def apply(id:String, to:JID, from:JID, kind:IQTypeEnumeration.Value):IQ =
			{
				val children = mutable.ListBuffer[Node]()
				//if ((null != subject) && (!subject.isEmpty)) children += <subject>{ subject }</subject>
				//if ((null != body) && (!body.isEmpty)) children += <body>{ body }</body>
				//if ((null != thread) && (!thread.isEmpty)) children += <thread>{ thread }</thread>
				var attributes:MetaData = new UnprefixedAttribute("id", Text(id), Null)
				attributes = attributes.append(new UnprefixedAttribute("to", Text(to), Null))
				attributes = attributes.append(new UnprefixedAttribute("from", Text(from), Null))
				attributes = attributes.append(new UnprefixedAttribute("type", Text(kind.toString), Null))				
				return new IQ(Elem(null, TAG, attributes, TopScope, children:_*))
			}	
			
			def error(id:String, to:JID, from:JID, errorCondition:ErrorCondition.Value):IQ = error(id, to, from, errorCondition, None)
			
			def error(id:String, to:JID, from:JID, errorCondition:ErrorCondition.Value, errorDescription:Option[String]):IQ =
			{
				// TODO: test this
				var attributes:MetaData = new UnprefixedAttribute("id", Text(id), Null)
				attributes = attributes.append(new UnprefixedAttribute("to", Text(to), Null))
				attributes = attributes.append(new UnprefixedAttribute("from", Text(from), Null))
				attributes = attributes.append(new UnprefixedAttribute("type", Text(MessageTypeEnumeration.Error.toString), Null))				
				return new IQ(Elem(null, TAG, attributes, TopScope, Error(errorCondition, errorDescription)))				
			}			
		}		
		
		class IQ(xml:Node) extends Stanza[IQ](xml)
		{
			val TypeEnumeration = IQTypeEnumeration
			
			
			final def error(condition:ErrorCondition.Value, description:Option[String]=None) = Message.error(this.id, this.from, this.to, condition, description)
		}
				
		final object IQTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Get = Value("get")
			val Set = Value("set")
			val Result = Value("result")
			val Error = Value("error")
		}		
		
	}
}
