package org.xmpp
{
	package codec
	{
		import scala.xml._
		
		import org.apache.mina.core.buffer.IoBuffer
		import org.apache.mina.core.session.IdleStatus
		import org.apache.mina.core.session.IoSession
		import org.apache.mina.filter.codec._
		import net.lag.naggati._
		import net.lag.naggati.Steps._
		
		import org.xmpp.protocol._
		import org.xmpp.component.Handshake
		
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
				//state.out.write(string) 
				//End
				val string = new String(buffer, "UTF-8")
				
				if (StreamHead.qualifies(string))
				{
					state.out.write(StreamHead(string)) 
					End
				}
  				else if (StreamTail.qualifies(string))
  				{
  					state.out.write(StreamTail())
  					End
  				}
  				else
  				{
  					try
  					{
  						val xml = XML.loadString(string)
						val message = xml.label match
						{
							case StreamError.tag => StreamError(xml)
							case Handshake.tag => Handshake(xml)
							case _ => Stanza(xml)
						}
						state.out.write(message) 
						End
  					}
  					catch
  					{
  						case pe:org.xml.sax.SAXParseException =>
  						{
  							// TODO: implement this
  							// parial xml recieved, wait for rest of it in the next packet
  							println("partial xml recieved " + string)
  							End
  						}
  						case e:Exception =>
  						{
  							// TODO: do something more intelligent here
  							println("internal error decoding packet " + string)
  							End
  						}
  						
  					}
  				}
			}
		})
	}
}