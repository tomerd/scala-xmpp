package org.simbit.xmpp
{
	package codec
	{		
		import scala.collection._
		import scala.xml._
		import scala.xml.pull._
		
		import org.apache.mina.core.buffer.IoBuffer
		import org.apache.mina.core.session.IdleStatus
		import org.apache.mina.core.session.IoSession
		import org.apache.mina.filter.codec._
	
		import net.lag.naggati._
		import net.lag.naggati.Steps._
		
		import org.simbit.xmpp.util._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.component.Handshake
		
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
		
		class XmppDecoder extends Decoder(XmppDecoder.decode)
		
		object XmppDecoder extends Logger
		{
			def decode = readAll(buffer => parse(new String(buffer, "UTF-8")))

			private def parse(input:String):Step = 
			{ 			
				if (StreamHead.qualifies(input))
				{
					state.out.write(StreamHead(input)) 
					End
				}
				else if (StreamTail.qualifies(input))
				{
					state.out.write(StreamTail())
					End
				}
				else
				{
					parseInternal(input)
				}
			}
			
			private def parseInternal(input:String):Step =
			{
				try
				{
					if (!state.data.contains("buffered")) state.data += "buffered" -> ""
					
					val candidate = state.data("buffered") + input
					parseXml(candidate) match
					{
						case Some(xmls) =>
						{
							// clear buffer
							state.data("buffered") = ""
							// write messages out
							val ouput = xmls.map( xml =>
							{
								xml.label match
								{
									case StreamError.tag => StreamError(xml)
									case Handshake.tag => Handshake(xml)
									case _ => Stanza(xml)
								}
							})
							
							val x = if (ouput.length > 1) ouput else ouput(0)
							state.out.write(x)
							End
						}
						case None =>
						{
							// partial xml, buffere till rest of required packets arrive
							error("partial xml found: " + input) 
							state.data("buffered") = candidate
							decode
						}
					}
				}
				catch
				{
					case e:Exception =>
					{
						// TODO: do something more intelligent here
						error("internal error decoding packet " + input + "\nerror: " + e)
						End
					}
				}
			}
			
			def parseXml(input:String):Option[Seq[Node]] = 
			{
				var level = 0
				var children = new mutable.HashMap[Int, mutable.ListBuffer[Node]]
				var attributes = new mutable.HashMap[Int, MetaData]
				var scope = new mutable.HashMap[Int, NamespaceBinding]
				
				var nodes = mutable.ListBuffer[Node]()
				
				try
				{
					// FIXME: using a customized version of XMLEventReadr as it is buggy, see 
					// http://scala-programming-language.1934581.n4.nabble.com/OutOfMemoryError-when-using-XMLEventReader-td2341263.html
					//  should be fixed in scala 2.9, need to test when it is released
					val tokenizer = new XMLEventReaderEx(scala.io.Source.fromString(input))	
					tokenizer.foreach( token =>
					{			
						token match
						{
							case tag:EvText => children(level) += new Text(tag.text)
							case tag:EvProcInstr => children(level) += new ProcInstr(tag.target, tag.text)
							case tag:EvComment => children(level) += new Comment(tag.text)
							case tag:EvEntityRef => children(level) += new EntityRef(tag.entity)
							case tag:EvElemStart => 
							{			
								level = level+1
								if (!attributes.contains(level)) attributes += level -> tag.attrs else attributes(level) = tag.attrs
								if (!scope.contains(level)) scope += level -> tag.scope else scope(level) = tag.scope
								if (!children.contains(level)) children += level -> new mutable.ListBuffer[Node]() else children(level) = new mutable.ListBuffer[Node]()
							}
							case tag:EvElemEnd => 
							{
								val node = Elem(tag.pre, tag.label, attributes(level), scope(level), children(level):_*)
								
								level = level-1
								if (0 == level)
								{
									nodes += node
								}
								else
								{
									children(level) += node		
								}				
							}
						}
					})
					
					return Some(nodes)
				}
				catch
				{
					case e:parsing.FatalError =>
					{
						// TODO: need to handle bad vs. partial xml, on the latter is important to us (for buffering)
						return None
					}
					case e:Exception =>
					{
						// FIXME: for testing
						return None
					}
				}
			}
		}
		
		
			
	}
}