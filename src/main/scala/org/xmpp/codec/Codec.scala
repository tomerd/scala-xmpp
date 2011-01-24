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
		
		import org.xmpp.protocol.Stanza
		
		object Codec
		{
			val encoder = new XmppEncoder
			val decoder = new XmppDecoder
		}
		
		class XmppEncoder extends ProtocolEncoder
		{
			def encode(session:IoSession, message:AnyRef, out:ProtocolEncoderOutput)
			{
				val buffer = IoBuffer.wrap(message.toString.getBytes)
				out.write(buffer)
			}
			
			def dispose(session:IoSession)
			{
				// TODO, implement this
			}
		}
		
		// FIXME: this currently assumes 1 packaet = 1 stanza, 
		// but this is a wrong assumption, need to buffer the content until start tag and end tag match
		class XmppDecoder extends Decoder(readAll
		{ buffer =>
			{
				val string = new String(buffer, "UTF-8")
				state.out.write(string) 
				End
			}
			/*
		    line.split(' ').first match 
			{
				case "HELO" => state.out.write(Request("HELO", line.split(' ')(1))); End
				case _ => throw new ProtocolError("Malformed request line: " + line)
			}
			*/
		})
	}
}