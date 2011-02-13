package org.simbit.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		object StreamError
		{
			val tag = "error"
			
			def apply(xml:Node):StreamError = new StreamError(xml)
		}
		
		class StreamError(xml:Node) extends XmlWrapper(xml)
		{
			// FIXME: implement this
		}
	}
}