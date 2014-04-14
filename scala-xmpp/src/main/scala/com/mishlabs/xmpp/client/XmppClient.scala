package com.mishlabs.xmpp
package client

import scala.collection._

import com.mishlabs.xmpp.util._

import com.mishlabs.xmpp.network._
import com.mishlabs.xmpp.protocol._
import com.mishlabs.xmpp.protocol.presence._
import com.mishlabs.xmpp.protocol.iq._
import com.mishlabs.xmpp.protocol.message._
import com.mishlabs.xmpp.protocol.extensions._

import com.mishlabs.xmpp.protocol.Protocol._

class DefaultXmppClientConfig extends NettyXmppClientConfig
{
    val timeout = 5*60*1000
    val maxReconnectAttempts = 5
    val keepAliveInterval = 60*1000
    val cleanupInterval = 5*60*1000
}

// FIXME: implement this for real
trait XmppClient extends NettyXmppClient
{
    private val DEFAULT_PORT = 5222

    private val logger = new Logger {}

    private var _jid:JID = null
    def jid = _jid

    private var _password:String = null

    final protected def identifier = _jid

    final protected def login(jid:JID, password:String)
    {
        login(jid, password, jid.domain, DEFAULT_PORT)
    }

    final protected def login(jid:JID, password:String, host:String, port:Int)
    {
        _jid = jid
        _password = password

        super.connect(host, port)
    }

    final protected def logout()
    {
        disconnect()
    }

    final override protected def handleConnected()
    {
        super.handleConnected()

        logger.debug(this.jid + " sending stream head")
        send(StreamHead("jabber:component:accept", Map("to" -> this.jid)))
    }

    final override protected def handle(packet:Packet)
    {
        super.handle(packet)

        try
        {
            packet match
            {
                case head:StreamHead =>
                {
                    logger.debug(this.jid + " received stream head")
                    /*
                    head.findAttribute("id") match
                    {
                        case Some(connectionId) => send(ComponentHandshake(connectionId, _secret))
                        case None => throw new Exception("invaild stream head, connection id not found")
                    }
                    */
                }
                case tail:StreamTail => logger.debug(this.jid + " received stream tail") // TODO, do something more intelligent here?
                //case handshake:Handshake => debug(this.jid + " handshake completed")
                case error:StreamError => throw new Exception("stream error: " + error.condition + ", " + error.description.getOrElse(""))
                case stanza:Stanza => handleStanza(stanza)
                case stanzas:Seq[Stanza] => stanzas.foreach( stanza => handleStanza(stanza) )
                case _ => throw new Exception("unknown xmpp packet")
            }
        }
        catch
        {
            case e =>
            {
                logger.error(this.jid + " error handling packet: " + e)
                disconnect()
            }
        }
    }

    private def handleStanza(stanza:Stanza)
    {
        stanza match
        {
            case presence:Presence => onPresence(presence)
            //case get @ Get(_, _, _, Some(request:disco.InfoRequest)) => handleDiscoInfo(get, request)
            //case get @ Get(_, _, _, Some(request:disco.ItemsRequest)) => handleDiscoItems(get, request)
            case iq:IQ => onIQ(iq)
            case message:Message => onMessage(message)
        }
    }

    protected def onPresence(presence:Presence) = Unit

    protected def onIQ(iq:IQ) = Unit

    protected def onMessage(message:Message) = Unit
}
