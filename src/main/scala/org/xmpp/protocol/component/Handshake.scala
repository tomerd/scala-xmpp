package org.xmpp
{
	package protocol.component
	{
		import scala.xml._
		
		import java.security.MessageDigest
		
		import org.xmpp.protocol._
		
		private object HandshakeHelper
		{
			val digest = MessageDigest.getInstance("SHA-1")
		}
		
		case class Handshake(connectionId:String, secret:String) extends XmlWrapper
		{
			def xml = <handshake>{ handshakeKey }</handshake>
			
			private def handshakeKey:String =
			{				
				def bytes2Hex(bytes:Array[Byte]):String = 
				{
					def cvtByte(b:Byte):String = (if (( b & 0xff ) < 0x10 ) "0" else "" ) + java.lang.Long.toString( b & 0xff, 16 ) 
					
					return bytes.map(cvtByte( _ )).mkString
				}
				
				val key = this.connectionId + this.secret
				HandshakeHelper.digest.update(key.getBytes("UTF-8"))
				return bytes2Hex(HandshakeHelper.digest.digest)
			}
		}
	}
}