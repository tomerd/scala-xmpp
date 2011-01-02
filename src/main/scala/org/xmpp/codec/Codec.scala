package org.xmpp
{
	package codec
	{
		import org.apache.mina.core.buffer.IoBuffer
		import org.apache.mina.core.session.IdleStatus
		import org.apache.mina.core.session.IoSession
		import org.apache.mina.filter.codec._
		import net.lag.naggati._
		import net.lag.naggati.Steps._
		
		case class Request(command:String, data:String)
		case class Response(data:IoBuffer)
		
		object Codec
		{
			val encoder = new XmppEncoder
			val decoder = new XmppDecoder			
		}
		
		class XmppEncoder extends ProtocolEncoder
		{
			def encode(session:IoSession, message:AnyRef, out:ProtocolEncoderOutput)
			{
				val buffer = message.asInstanceOf[Response].data
				out.write(buffer)
			}
			
			def dispose(session:IoSession)
			{
				// TODO, implement this
			}
		}
		
		class XmppDecoder extends Decoder(readLine(true, "ISO-8859-1") 
		{ line =>
		    line.split(' ').first match 
			{
				case "HELO" => state.out.write(Request("HELO", line.split(' ')(1))); End
				case _ => throw new ProtocolError("Malformed request line: " + line)
			}
		})
	}
}