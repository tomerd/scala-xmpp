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
			
			// TODO: implement various data types here
			def apply(parameters:Seq[String]):MethodResponse = 
			{
				val children = mutable.ListBuffer[Node]()
				
				val parametersNodes = parameters.map( parameter => <param><value>{ parameter }</value></param> )
				val parametersNode = Elem(null, "params", Null, TopScope, parametersNodes:_*)
				children += parametersNode
				
				apply(Builder.build(Elem(null, tag, Null, TopScope, children:_*)))
			}
			
			def apply(xml:Node):MethodResponse = new MethodResponse(xml)
		}
	
		// TODO: implement various data types here
		class MethodResponse(xml:Node) extends Query(xml)
		{
			private val methodNode = (xml \ MethodResponse.tag)(0)
			
			val parameters:Seq[Parameter] = (this.methodNode \\ Parameter.tag).map( node => Parameter(node) )
		}
	}	
}