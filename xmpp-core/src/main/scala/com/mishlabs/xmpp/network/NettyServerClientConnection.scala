package com.mishlabs.xmpp
package network

import javax.net.ssl.SSLEngine

import org.jboss.netty.channel._
import org.jboss.netty.channel.group.ChannelGroup
import org.jboss.netty.handler.timeout._
import org.jboss.netty.util.HashedWheelTimer

import com.twitter.naggati.ProtocolError

import util._

// TODO: consolidate client and server handler classes
abstract class NettyServerClientConnection[M](channelGroup:ChannelGroup, timer:HashedWheelTimer, timeout:Option[Int]) extends ChannelUpstreamHandler
{
    var _channel:Option[Channel] = None
    protected def channel:Option[Channel] = _channel

    private val logger = new Logger()

    def handleUpstream(context:ChannelHandlerContext, channelEvent:ChannelEvent)
    {
        channelEvent match
        {
            case event:MessageEvent => handleMessage(event.getMessage.asInstanceOf[M])
            case event:ExceptionEvent =>
            {
                event.getCause match
                {
                    case e:ProtocolError => handleProtocolError(e)
                    case e:java.nio.channels.ClosedChannelException => disconnected()
                    case e:javax.net.ssl.SSLException => handleCommunicationError(e)
                    case e:java.io.IOException =>
                    {
                        if ("Connection reset by peer" != e.getMessage)
                        {
                            logger.error("netty connection exception. disconnecting.", e)
                        }
                        disconnected()
                    }
                    case e => handleCommunicationError(e)
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
                        //channelGroup.add(event.getChannel)
                        handleConnected()
                    }
                    else
                    {
                        logger.trace("netty connection state: disconnected. " + event.getChannel)
                        disconnected()
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
                        logger.trace("netty connection state: open. " + event.getChannel)

                        _channel = Some(event.getChannel)

                        if (timeout.getOrElse(0) > 0)
                        {
                            _channel.get.getPipeline.addFirst("idle", new IdleStateHandler(timer, 0, 0, timeout.get))
                        }

                        channelGroup.add(_channel.get)
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
                event.getChannel.close()
                disconnected()
            }
            case event => context.sendUpstream(event)
        }
    }

    def send(message:M)
    {
        try
        {
            if (_channel.isEmpty) return
            if (!_channel.get.isWritable) return
            _channel.get.write(message)
        }
        catch
        {
            case e => handleSendError(e)
        }
    }

    private def disconnected()
    {
        _channel = None
        handleDisconnected()
    }

    // subclass delegates
    protected def handleConnected()
    protected def handleDisconnected()
    protected def handleMessage(message:M)
    protected def handleProtocolError(e:Throwable)
    protected def handleCommunicationError(e:Throwable)
    protected def handleSendError(e:Throwable)
}
