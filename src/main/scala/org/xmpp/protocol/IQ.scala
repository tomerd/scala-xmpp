package org.xmpp
{
	package protocol
	{
		import scala.xml._
			
		class IQ(literal:Node) extends Stanza[IQ](literal)
		{
			val typeResolver = IQTypeEnumeration
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
