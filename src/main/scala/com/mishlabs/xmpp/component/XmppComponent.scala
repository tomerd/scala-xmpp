package com.mishlabs.xmpp
package component

import scala.collection._

import com.mishlabs.xmpp.util._

import com.mishlabs.xmpp.network._
import com.mishlabs.xmpp.protocol._
import com.mishlabs.xmpp.protocol.presence._
import com.mishlabs.xmpp.protocol.iq._
import com.mishlabs.xmpp.protocol.message._
import com.mishlabs.xmpp.protocol.extensions._

import com.mishlabs.xmpp.protocol.Protocol._

class DefaultXmppComponentConfig extends NettyXmppClientConfig
{
    val timeout = 5*60*1000
    val maxReconnectAttempts = 5
    val keepAliveInterval = 60*1000
    val cleanupInterval = 5*60*1000
}

trait XmppComponent extends NettyXmppClient
{
    protected val identities:Seq[disco.Identity] = Nil
    protected val features:Seq[disco.Feature] = Nil
    // TODO, not sure if i like this, perhaps need to leave it up to the component to decide if and when it wants to register the extension builders
    protected val extensionsBuilders:Seq[ExtensionBuilder[_ <: Extension]] = Nil

    private var _jid:JID = null
    def jid = _jid

    private var _subdomain:String = null
    def subdomain = _subdomain

    private var _secret:String = null

    private val logger = new Logger

    final protected def identifier = _jid

    def startup(subdomain:String, host:String, port:Int, secret:String /*, timeout:Int=0*/)
    {
        _jid = JID(null, subdomain + "." + host, null)
        _subdomain = subdomain
        _secret = secret

        // TODO, not sure if i like this, perhaps need to leave it up to the component to decide if and when it wants to register the extension builders
        extensionsBuilders.foreach( builder => ExtensionsManager.registerBuilder(builder) )

        connect(host, port /*, timeout*/)

        // TODO: add hook for implementations
    }

    def shutdown()
    {
        try
        {
            send(StreamTail())
        }
        catch
        {
            // TODO: do something intelligent here
            case e:Exception => logger.error(this.jid + " shutdown error " + e)
        }
        finally
        {
            disconnect()
        }
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

                    head.findAttribute("id") match
                    {
                        case Some(connectionId) => send(ComponentHandshake(connectionId, _secret))
                        case None => throw new Exception("invalid stream head, connection id not found")
                    }
                }
                case tail:StreamTail => logger.debug(this.jid + " received stream tail") // TODO, do something more intelligent here?
                case handshake:Handshake => logger.info(this.jid + " handshake complete, xmpp route established")
                case error:StreamError => throw new Exception("stream error: " + error.condition + ", " + error.description.getOrElse(""))
                case stanza:Stanza => handleStanza(stanza)
                case stanzas:Seq[Stanza] => stanzas.foreach( stanza => handleStanza(stanza) )
                case _ => throw new Exception("unknown xmpp packet")
            }
        }
        catch
        {
            case e:Exception =>
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
            case get @ Get(_, _, _, Some(request:disco.InfoRequest)) => handleDiscoInfoRequest(get, request)
            case get @ Get(_, _, _, Some(request:disco.ItemsRequest)) => handleDiscoItemsRequest(get, request)
            case iq:IQ => onIQ(iq)
            case message:Message => onMessage(message)
        }
    }

    private def handleDiscoInfoRequest(get:Get, request:disco.InfoRequest)
    {
        /*
        if (request.to == this.jid)
        {
            send(request.result(infoRequest.result(this.identities, this.features)))
        }
        else getChildDiscoInfo(request.to, infoRequest) match
        {
            case Some(info) => send(request.result(info))
            case _ => // do nothing
        }
        */

        get.to match
        {
            case Some(jid) if jid == this.jid =>
            {
                send(get.result(request.result(this.identities, this.features)))
            }
            case Some(jid) => getChildDiscoInfo(jid, request) match
            {
                case Some(info) => send(get.result(info))
                case _ => // do nothing
            }
            case _ => // do nothing
        }
    }

    private def handleDiscoItemsRequest(get:Get, request:disco.ItemsRequest)
    {
        /*
        if (request.to == this.jid) getDiscoItems(itemsRequest) match
        {
            case Some(items) => send(request.result(items))
            case _ => // do nothing
        }
        else getChildDiscoItems(request.to, itemsRequest) match
        {
            case Some(items) => send(request.result(items))
            case _ => // do nothing
        }*/

        get.to match
        {
            case Some(jid) if jid == this.jid => getDiscoItems(request) match
            {
                case Some(items) => send(get.result(items))
                case _ => // do nothing
            }
            case Some(jid) => getChildDiscoItems(jid, request) match
            {
                case Some(items) => send(get.result(items))
                case _ => // do nothing
            }
            case _ => // do nothing
        }

    }

    protected def getDiscoItems(request:disco.ItemsRequest):Option[disco.ItemsResult] = None

    protected def getChildDiscoItems(jid:JID, request:disco.ItemsRequest):Option[disco.ItemsResult] = None

    protected def getChildDiscoInfo(jid:JID, request:disco.InfoRequest):Option[disco.InfoResult] = None

    protected def onPresence(presence:Presence) = Unit

    protected def onIQ(iq:IQ) = Unit

    protected def onMessage(message:Message) = Unit
}