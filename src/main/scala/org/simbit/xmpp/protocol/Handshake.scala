package org.simbit.xmpp
{
	package component
	{
		import java.security.MessageDigest
		
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		
		object Handshake
		{
			val tag = "handshake"
				
			def apply(key:String):Handshake =
			{
				return new Handshake(<handshake>{ key }</handshake>)
			}
			
			def apply(xml:Node):Handshake = new Handshake(xml)
			
			def apply():Handshake = apply(<handshake/>)		
		}
		
		class Handshake(xml:Node) extends XmlWrapper(xml)
		{
			val key:String =  this.xml.text
		}
	}
}