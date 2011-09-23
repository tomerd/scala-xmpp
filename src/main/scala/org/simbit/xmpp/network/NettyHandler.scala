/*
influenced by Robey Pointer / twitter kestrel implmentation
*/

package org.simbit.xmpp
{
	package network
	{
		import java.io.IOException
		
		import java.net.InetSocketAddress
		import java.util.concurrent.TimeUnit
		
		import scala.collection.mutable
		
		import org.jboss.netty.channel._
		//import org.jboss.netty.channel.group.ChannelGroup
		import org.jboss.netty.handler.timeout._
		import org.jboss.netty.util.HashedWheelTimer
		
		import com.twitter.conversions.time._
		import com.twitter.naggati.ProtocolError
		//import com.twitter.util.{Duration, Time}
		
		import org.simbit.xmpp.util._
		
		abstract class NettyHandler[M] extends ChannelUpstreamHandler with Logger
		{
			protected def timeout:Option[Int] = None
			
			def handleUpstream(context:ChannelHandlerContext, channelEvent:ChannelEvent) 
			{
				channelEvent match 
				{
					case event:MessageEvent => handle(event.getMessage.asInstanceOf[M])
					case event:ExceptionEvent =>
					{
						event.getCause match 
						{
							case _:ProtocolError => handleProtocolError
							case e:java.nio.channels.ClosedChannelException => handleDisconnected
							//case e2:IOException => error("I/O exception on session %d:%s", sessionId, e2.toString)
							case e =>
							{
								error("exception caught on netty session " + event.getChannel + " " + e)
								handleCommunicationException(e)
							}
							//event.getChannel.close
						}
					}
					case event:ChannelStateEvent =>
					{
						if (ChannelState.CONNECTED == event.getState)
						{
							if (event.getValue != null)
							{
								debug("netty connection state: connected. " + event.getChannel)								
								info("new netty session " + event.getChannel)
								
								//channelGroup.add(event.getChannel)
								handleConnected
							}
							else
							{
								debug("netty connection state: disconnected. " + event.getChannel)							
								handleDisconnected	
							}
						}						
						else if (ChannelState.BOUND == event.getState) 
						{
							if (event.getValue != null)
							{
								debug("netty connection state: bound. " + event.getChannel)
							}
							else
							{
								debug("netty connection state: unbound. " + event.getChannel)	
							}
						}
						else if (ChannelState.OPEN == event.getState)
						{
							if (event.getValue == true)
							{
								debug("netty connection state: ope.n " + event.getChannel)
								//val channel = event.getChannel
								
								if (timeout.isDefined) 
								{
									// this means no timeout will be at better granularity than 10ms.
									val timer = new HashedWheelTimer(10, TimeUnit.MILLISECONDS)
									// timeout in seconds
									event.getChannel.getPipeline.addFirst("idle", new IdleStateHandler(timer, 0, 0, timeout.get/1000))
								}								
							}
							else
							{
								debug("netty session state: closed. " + event.getChannel)
							}
						}
					}
					case event:IdleStateEvent =>
					{
						debug("netty session timeout " + event.getChannel)
						//channel.close
						event.getChannel.close
					}
					case event => context.sendUpstream(event)
				}
			}
			
			protected def handleConnected
			protected def handleDisconnected
			protected def handle(message:M)
			protected def handleProtocolError
			protected def handleCommunicationException(e:Throwable)
			//protected def shutdown
			
		}
	}
}