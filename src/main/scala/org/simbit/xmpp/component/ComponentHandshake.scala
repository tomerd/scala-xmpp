package org.simbit.xmpp
{
	package component
	{
		import java.security.MessageDigest
		
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		
		protected object ComponentHandshake
		{			
			private val digest = MessageDigest.getInstance("SHA-1")
					
			def apply(connectionId:String, secret:String):Handshake =
			{
				val key = createHandshakeKey(connectionId, secret)
				return new Handshake(<handshake>{ key }</handshake>)
			}
						
			private def createHandshakeKey(connectionId:String, secret:String):String =
			{
				def bytes2Hex(bytes:Array[Byte]):String = 
				{
					def cvtByte(b:Byte):String = (if (( b & 0xff ) < 0x10 ) "0" else "" ) + java.lang.Long.toString( b & 0xff, 16 ) 
					
					return bytes.map(cvtByte( _ )).mkString
				}
				
				val key = connectionId + secret
				digest.update(key.getBytes("UTF-8"))
				return bytes2Hex(digest.digest)
			}
		}
		
	}
}