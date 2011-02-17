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
		
		object MethodCall 
		{
			val tag = "methodCall"
			
			// TODO: implement various data types here
			def apply(name:String, parameters:Seq[String]):MethodCall = 
			{
				val children = mutable.ListBuffer[Node]()
				children += <methodName>{ name }</methodName>
				
				val parametersNodes = parameters.map( parameter => <param><value>{ parameter }</value></param> )
				val parametersNode = Elem(null, "params", Null, TopScope, parametersNodes:_*)
				children += parametersNode
				
				apply(Builder.build(Elem(null, tag, Null, TopScope, children:_*)))
			}
			
			def apply(xml:Node):MethodCall = new MethodCall(xml)
		}
	
		// TODO: implement various data types here 
		class MethodCall(xml:Node) extends Query(xml)
		{
			private val methodNode = (xml \ MethodCall.tag)(0)
			
			val name:String = (this.methodNode \ "methodName").text
			
			val parameters:Seq[Parameter] = (this.methodNode \\ Parameter.tag).map( node => Parameter(node) )
		}
	}	
}