package org.simbit.xmpp
{
	package protocol.extensions.rpc
	{
		import scala.collection._
		import scala.xml._
	
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.presence._
		import org.simbit.xmpp.protocol.extensions._
	
		import org.simbit.xmpp.protocol.Protocol._
		
		object MethodResponse 
		{
			val tag = "methodResponse"
			
			def apply(value:Parameter):MethodResponse = 
			{
				val parametersNode = Elem(null, "params", Null, TopScope, value)
				apply(Builder.build(Elem(null, tag, Null, TopScope, parametersNode)))
			}
			
			def apply(xml:Node):MethodResponse = new MethodResponse(xml)
		}
	
		// TODO: implement various data types here
		class MethodResponse(xml:Node) extends Query(xml)
		{
			private val methodNode = (xml \ MethodResponse.tag)(0)
			
			val value:Parameter = Parameter((this.methodNode \\ Parameter.tag)(0))
		}
	}	
}