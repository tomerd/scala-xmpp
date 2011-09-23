package org.simbit.xmpp
{
	package network
	{
		import scala.collection._
		import scala.xml._
		import scala.xml.pull._
				
		import org.jboss.netty.buffer.ChannelBuffer
		import org.jboss.netty.buffer.ChannelBuffers
		
		import com.twitter.naggati._
		import com.twitter.naggati.Stages._
		
		import org.simbit.xmpp.util._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.component.Handshake
		
		object XmppCodec
		{
			def getCodec() = new Codec(decode, encode)
			
			/*
			val encode = new Encoder[Packet] 
			{
				def encode(packet:Packet)
				{
					val bytes = packet.toString.getBytes
					val buffer = ChannelBuffers.buffer(bytes.length)
					buffer.writeBytes(bytes)
					Some(buffer)
				}
			}
			*/
			
			val encode:PartialFunction[Any, ChannelBuffer] = 
			{
				case packet:Packet =>
				{
					val bytes = packet.toString.getBytes
					val buffer = ChannelBuffers.buffer(bytes.length)
					buffer.writeBytes(bytes)
					buffer
				}
				case text:String =>
				{
					val bytes = text.getBytes
					val buffer = ChannelBuffers.buffer(bytes.length)
					buffer.writeBytes(bytes)
					buffer
				}
			}
			
			val decode = parse("")
			
			def parse(buffer:String):Stage = 
			{ 
				// FIXME: need to figure out how to read the entire available buffer instead of byte-by-byte
				// I sent an email to author of naggati2 library
				readBytes(1) { bytes =>
					
					val input = buffer + new String(bytes, "UTF-8")
					
					if (!input.endsWith(">"))
					{
						// assume partial packet. buffer till rest of required packets arrive
						parse(input)
					}
					else if (StreamHead.qualifies(input))
					{
						Emit(StreamHead(input))
					}
					else if (StreamTail.qualifies(input))
					{
						Emit(StreamTail())
					}
					else
					{
						try
						{
							parseXml(input) match
							{
								case Some(xmls) =>
								{
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
									
									Emit(if (ouput.length > 1) ouput else ouput(0))							
								}
								case None =>
								{
									// assume partial packet. buffer till rest of required packets arrive
									parse(input)
								}
							}
						}
						catch
						{
							case e:Exception =>
							{
								// TODO: do something more intelligent here
								error("internal error decoding packet " + input + "\nerror: " + e)
								throw new ProtocolError("internal error decoding packet " + input + "\nerror: " + e)
							}
						}
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
					
					return if (nodes.length > 0) Some(nodes) else None
				}
				catch
				{
					// TODO: need to handle bad vs. partial xml, on the latter is important to us (for buffering)
					case e:parsing.FatalError => None
					// FIXME: for testing
					case e:Exception =>  None
				}
			}
		}
					
	}
}