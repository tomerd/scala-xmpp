package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._
		
		abstract class Extension(xml:Node) extends XmlWrapper(xml)
		{
			val name:String =  this.xml.label
			
			val namespace:Option[String] = this.xml.scope.uri
		}
	}
}