package org.xmpp
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
		
		class XmppDecoder extends Decoder(XmppDecoder.decode)
		
		object XmppDecoder
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
					
					val buffered = state.data("buffered")
					val candidate = buffered + input
					
					var level = 0
					var children = new mutable.HashMap[Int, mutable.ListBuffer[Node]]
					var attributes = new mutable.HashMap[Int, MetaData]
					var scope = new mutable.HashMap[Int, NamespaceBinding]
					
					var result:Option[Tuple2[Node, Option[Int]]] = None
					
					//FIXME: XmlPull is buggy and crahses on bad (or partial!) xml
					val tokenizer = new XMLEventReader(scala.io.Source.fromString(candidate))						
					while (tokenizer.hasNext && result.isEmpty)
					{			
						tokenizer.next match
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
									// deal with leftover
									val leftover = tokenizer.hasNext match
									{
										case true => Some(tokenizer.indexOf(tag))
										case false => None
									}
									
									result = Some(Tuple2(node, leftover))
								}
								else
								{
									children(level) += node		
								}				
							}
						}
					}
										
					result match
					{
						case Some(tuple) =>
						{
							// clear buffer
							state.data("buffered") = ""
							// write message out
							val ouput = tuple._1.label match
							{
								case StreamError.tag => StreamError(tuple._1)
								case Handshake.tag => Handshake(tuple._1)
								case _ => Stanza(tuple._1)
							}								
							state.out.write(ouput)
							
							// deal with leftover
							if (!tuple._2.isEmpty)
							{
								// FIXME: implement this
								val index = tuple._2.get
								// FIXME, not sure why, but this is the only way i was able to get a stable copy of the iterator
								val leftover = new XMLEventReader(scala.io.Source.fromString(candidate)).drop(index).mkString
								println("leftover found: " + leftover)
								End // this should be replaced with recursive call with the lefover itself
							}
							else
							{
								End
							}
						}
						case None =>
						{
							// deal with parial xml
							println("partial found: " + input) 
							state.data("buffered") = candidate
							decode
						}
					}
					
					/*
					val buffered = state.data.getOrElse("buffered", "")
					val candidate = buffered + input					
					
					if (candidate.substring(candidate.length-2, candidate.length-1) != ">")
					{
						// deal with partial xml
						if (!state.data.contains("buffered")) state.data += "buffered" -> candidate else state.data("buffered") = candidate
						decode						
					}
					else
					{
						if (!leftover.isEmpty)
						{
							// deal with leftover xml
							// FIXME: implement this
							println("leftover")
						}
						else
						{
							// parse message
							val xml = XML.loadString(candidate)
							val output = xml.label match
							{
								case StreamError.tag => StreamError(xml)
								case Handshake.tag => Handshake(xml)
								case _ => Stanza(xml)
							}
							
							// clear buffer
							if (state.data.contains("buffered")) state.data("buffered") = ""
							// write message out
							state.out.write(output)
							End	
						}
					}
					*/
				}
				catch
				{
					case e:Exception =>
					{
						// TODO: do something more intelligent here
						println("internal error decoding packet " + input + "\nerror: " + e)
						End
					}
				}
			}
		}
		
		
			
	}
}