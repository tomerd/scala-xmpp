/*
influenced by Robey Pointer / twitter kestrel implmentation
*/

package com.mishlabs.xmpp
package network

import java.util.concurrent.TimeUnit

import org.jboss.netty.channel._
import org.jboss.netty.handler.timeout._
import org.jboss.netty.util.HashedWheelTimer

import com.twitter.naggati.ProtocolError

import com.mishlabs.xmpp.util._

// TODO: consolidate client and server handler classes
abstract class NettyClientConnection[M] extends ChannelUpstreamHandler
{
    protected def timeout:Option[Int] = None

    private val logger = new Logger()

    def handleUpstream(context:ChannelHandlerContext, channelEvent:ChannelEvent)
    {
        channelEvent match
        {
            case event:MessageEvent => handle(event.getMessage.asInstanceOf[M])
            case event:ExceptionEvent =>
            {
                event.getCause match
                {
                    case _:ProtocolError => handleProtocolError()
                    case e:java.nio.channels.ClosedChannelException => handleDisconnected()
                    //case e2:IOException => error("I/O exception on session %d:%s", sessionId, e2.toString)
                    case e =>
                    {
                        logger.error("exception caught on netty session " + event.getChannel + " " + e)
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
                        logger.trace("netty connection state: connected. " + event.getChannel)
                        logger.trace("new netty session " + event.getChannel)

                        //channelGroup.add(event.getChannel)
                        handleConnected()
                    }
                    else
                    {
                        logger.trace("netty connection state: disconnected. " + event.getChannel)
                        handleDisconnected()
                    }
                }
                else if (ChannelState.BOUND == event.getState)
                {
                    if (event.getValue != null)
                    {
                        logger.trace("netty connection state: bound. " + event.getChannel)
                    }
                    else
                    {
                        logger.trace("netty connection state: unbound. " + event.getChannel)
                    }
                }
                else if (ChannelState.OPEN == event.getState)
                {
                    if (event.getValue == true)
                    {
                        logger.trace("netty connection state: ope.n " + event.getChannel)
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
                        logger.trace("netty session state: closed. " + event.getChannel)
                    }
                }
            }
            case event:IdleStateEvent =>
            {
                logger.trace("netty session timeout " + event.getChannel)
                //channel.close
                event.getChannel.close
            }
            case event => context.sendUpstream(event)
        }
    }

    // sublclass delegates
    protected def handleConnected()
    protected def handleDisconnected()
    protected def handle(message:M)
    protected def handleProtocolError()
    protected def handleCommunicationException(e:Throwable)
}
