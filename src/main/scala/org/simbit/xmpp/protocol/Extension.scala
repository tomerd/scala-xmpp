package org.simbit.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		abstract class Extension(xml:Node) extends XmlWrapper(xml)
		{
			val tag:String =  this.xml.label
			
			val namespace:Option[String] = this.xml.scope.uri
		}
	}
}