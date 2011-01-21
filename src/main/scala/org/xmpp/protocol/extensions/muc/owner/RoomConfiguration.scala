package org.xmpp
{
	package protocol.extensions.muc
	{
		package owner
		{
			import scala.collection._
			import scala.xml._
		
			import org.xmpp.protocol._
			import org.xmpp.protocol.presence._
			import org.xmpp.protocol.extensions._
			import org.xmpp.protocol.extensions.forms._
		
			import org.xmpp.protocol.Protocol._
			
			object RoomConfiguration 
			{			
				def apply(form:Form):RoomConfiguration =
				{
					return apply(Builder.build(form))
				}
				
				def apply(xml:Node):RoomConfiguration = RoomConfiguration(xml)
			}
			
			class RoomConfiguration(xml:Node) extends Query(xml)
			{	
				val form:Form = forms.Builder(xml.child(0))
			}
		}
	}	
}