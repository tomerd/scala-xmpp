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
		
		class XmppDecoder extends Decoder(readAll
		{ buffer =>
			{
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
						var level = 0
						var children = new mutable.HashMap[Int, mutable.ListBuffer[Node]]
						var attributes = new mutable.HashMap[Int, MetaData]
						var scope = new mutable.HashMap[Int, NamespaceBinding]
						
						var result:Option[Tuple2[AnyRef, Option[Iterator[XMLEvent]]]] = None
						val tokenizer = new XMLEventReader(scala.io.Source.fromString(string))
						
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
											case true => Some(tokenizer.slice(tokenizer.indexOf(tag), tokenizer.length))
											case false => None
										}
										
										// parsed message
										val message = node.label match
										{
											case StreamError.tag => StreamError(node)
											case Handshake.tag => Handshake(node)
											case _ => Stanza(node)
										}
										
										result = Some(Tuple2(message, leftover))
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
  								// deal with leftover
  								if (!tuple._2.isEmpty)
  								{
	  								// FIXME: implement this
	  								println("tail found")
	  								val save = tuple._2.get
  								}
  								
  								// write message out
  								state.out.write(tuple._1)
  								End
  							}
  							case None =>
  							{
  								// deal with parial
  								// FIXME: implement this
  								println("partial found")
  								val save = tokenizer
  								End
  							}
						}
						
					}
					catch
					{
						case e:Exception =>
						{
							// TODO: do something more intelligent here
							println("internal error decoding packet " + string + "\nerror: " + e)
							End
						}
					}
				}
			}
		})
		
	}
}