package org.simbit.xmpp
{
	package protocol.extensions.rpc
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.message._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		private[xmpp] object Builder extends ExtensionBuilder[Query]
		{
			val tag = Query.tag
			val namespace = "jabber:iq:rpc"
			
			def apply(xml:Node):Query = 
			{
				if (1 == (xml \ MethodCall.tag).length)
				{
					return MethodCall(xml)
				}
				else if (1 == (xml \ MethodResponse.tag).length)
				{
					return MethodResponse(xml)
				}
				else
				{
					return Query(xml)
				}
			}
		}
		
	}
}
