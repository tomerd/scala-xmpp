package com.mishlabs.xmpp
package server

import scala.collection._

import java.security.KeyStore
import java.util.concurrent.atomic.AtomicLong

import network.{NettyXmppServerConfig, NettyXmppServer}

import protocol._
import com.mishlabs.xmpp.protocol.iq._
import extensions._

import util.Logger
import scala.Some
import sun.misc.BASE64Decoder

class DefaultXmppServerConfig extends NettyXmppServerConfig
{
    val clientTimeout = 5*60*1000
}

trait XmppServer extends NettyXmppServer[Long]
{
    private val DEFAULT_PORT = 5222

    private var _domain:String = null
    def domain = _domain

    private var state = 0

    private val logger = new Logger

    def startup(domain:String, host:String, keystore:KeyStore, keystorePassword:String)
    {
        startup(domain, host, DEFAULT_PORT, keystore, keystorePassword)
    }

    def startup(domain:String, host:String, port:Int, keystore:KeyStore, keystorePassword:String)
    {
        logger.info("xmpp server starting up")

        _domain = domain
        super.startup(host, port, keystore, keystorePassword)

        logger.info("xmpp server started. listening on %s:%s".format(host, port))
    }

    override def shutdown()
    {
        try
        {
            logger.info("xmpp server shutting down")
            super.shutdown()
            logger.info("xmpp server is down")
        }
        catch
        {
            case e => logger.error("failed shutting down xmpp server", e)
        }
    }

    // NettyXmppServer

    final protected def handleNewConnection(connectionId:Long, nettyConnection:NettyXmppServerClientConnection)
    {
        logger.trace("new xmpp connection %s".format(connectionId))
        val connection = new ClientConnectionImpl(connectionId, nettyConnection)
        ConnectionsManager.add(connectionId, connection)
        onClientConnected(connection)
    }

    final protected def handleDisconnect(connectionId:Long)
    {
        logger.trace("xmpp connection %s disconnected".format(connectionId))
        ConnectionsManager.get(connectionId) match
        {
            case Some(client) =>
            {
                onClientDisconnected(client)
                ConnectionsManager.remove(connectionId)
            }
            case _ => logger.warn("unknown xmpp connection %s".format(connectionId))
        }
    }

    final protected def handlePacket(connectionId:Long, packet:Packet)
    {
        ConnectionsManager.get(connectionId) match
        {
            case Some(connection) => connection.handlePacket(packet)
            case _ => logger.warn("unknown xmpp connection %s".format(connectionId))
        }
    }

    final protected def handleProtocolError(connectionId:Long, e:Throwable)
    {
        handleConnectionError(connectionId, e)
    }

    final protected def handleCommunicationError(connectionId:Long, e:Throwable)
    {
        handleConnectionError(connectionId, e)
    }

    final protected def handleSendError(connectionId:Long, e:Throwable)
    {
        handleConnectionError(connectionId, e)
    }

    final protected def generateNewConnectionId():Long = ConnectionsManager.generateNewId()


    private def handleConnectionError(connectionId:Long, e:Throwable)
    {
        logger.error("communication error on xmpp connection %s".format(connectionId), e)
        ConnectionsManager.get(connectionId) match
        {
            case Some(connection) => connection.handleError(e)
            case _ => logger.warn("unknown xmpp connection %s".format(connectionId))
        }
    }

    // subclass delegates

    protected def onClientConnected(connection:ClientConnection)

    protected def onClientDisconnected(connection:ClientConnection)

    // client connections management

    private object ConnectionsManager
    {
        private val connections = new mutable.HashMap[Long, ClientConnectionImpl]() with mutable.SynchronizedMap[Long, ClientConnectionImpl]
        private val counter:AtomicLong = new AtomicLong()

        def get(id:Long):Option[ClientConnectionImpl] =
        {
            connections.get(id)
        }

        def add(id:Long, connection:ClientConnectionImpl)
        {
            connections += id -> connection
        }

        def remove(id:Long)
        {
            connections -= id
        }

        def generateNewId():Long =
        {
            System.currentTimeMillis + counter.incrementAndGet
        }
    }

    private class ClientConnectionImpl(val id:Long, val nettyConnection:NettyXmppServerClientConnection) extends ClientConnection
    {
        private var _username:Option[String] = None
        private var _jid:Option[JID] = None

        def jid = _jid

        // make sure some delegate is in place
        delegate = Some(new ClientConnectionDelegate {})

        def handlePacket(packet:Packet)
        {
            packet match
            {
                // FIXME: implement state machine instead of this spaghetti style
                case p:StreamHead => handleStreamHead(p)
                case p:StreamTail => handleStreamTail(p)
                case p:StartTls => handleStartTls(p)
                case p:TlsFailure => handleTlsFailure(p)
                case p:SaslAuth => handleSaslAuth(p)
                case p:Stanza => handleStanza(p)
                case p:Seq[Stanza] => p.foreach( handleStanza(_) )
                case p:StreamError => handleStreamError(p)
                case _ => throw new Exception("xmpp connection %s received unknown packet %s".format(this.id, packet))
            }
        }

        def handleError(e:Throwable)
        {
            this.delegate.get.onError(e)
        }

        def send(packet:Packet)
        {
            nettyConnection.send(packet)
        }

        def close()
        {
            nettyConnection.close()
        }

        // private

        private def handleStreamHead(head:StreamHead)
        {
            logger.trace("xmpp connection %s received stream head".format(this.id))
            val attributes = mutable.HashMap( "id" -> this.id.toString, "from" -> XmppServer.this.domain )
            head.findAttribute("from") match
            {
                case Some(from) if !from.isEmpty => attributes +=  "to" -> from
                case _ => // ignore
            }
            head.findAttribute("version") match
            {
                case Some(version) if "1.0" == version => attributes +=  "version" -> "1.0"
                case _ => state = -1 // legacy mode (xep-0078)
            }
            send(StreamHead("jabber:client", attributes))

            if (0 == state)
            {
                sendFeatures(<starttls xmlns={ Tls.namespace }>
                                <required/>
                             </starttls>)
            }
            else if (1 == state)
            {
                sendFeatures(<mechanisms xmlns={ Sasl.namespace }>
                                <mechanism>{ SaslMechanism.Plain.toString }</mechanism>
                             </mechanisms>)
            }
            else if (2 == state)
            {
                sendFeatures(<bind xmlns={ bind.BindBuilder.namespace }>
                                <required/>
                             </bind>)
            }
        }

        private def sendFeatures(features:Seq[scala.xml.Node])
        {
            send(Features(features))
        }

        private def handleStreamTail(tail:StreamTail)
        {
            // TODO: decide what needs to be done here, if anything
            logger.trace("xmpp connection %s received stream tail".format(this.id))
        }

        private def handleStartTls(tls:StartTls)
        {
            logger.trace("xmpp connection %s received start tls".format(this.id))

            state = 1
            send(TlsProceed())
            nettyConnection.secure()
        }

        private def handleTlsFailure(failure:TlsFailure)
        {
            logger.trace("xmpp connection %s received tls failure".format(this.id))
            send(StreamTail())
            close()
        }

        private def handleSaslAuth(sasl:SaslAuth)
        {
            logger.trace("xmpp connection %s received %s sasl auth".format(this.id, sasl.mechanism))

            try
            {
                state = 2
                // TODO: support other mechanisms
                sasl.mechanism match
                {
                    case SaslMechanism.Plain if sasl.value != null && !sasl.value.isEmpty =>
                    {
                        //http://tools.ietf.org/html/rfc4616
                        val decoded = new String(new sun.misc.BASE64Decoder().decodeBuffer(sasl.value), "UTF8")
                        val parts = decoded.split("\0")
                        if (3 != parts.length) throw new SaslAuthenticationError(SaslErrorCondition.MalformedRequest)
                        this.delegate.get.authenticate(new AuthenticationRequest(parts(1), parts(2))) match
                        {
                            case AuthenticationResult.Success => _username = Some(parts(1)); send(SaslSuccess())
                            case AuthenticationResult.NotAuthorized => throw new SaslAuthenticationError(SaslErrorCondition.NotAuthorized)
                            case AuthenticationResult.CredentialsExpired => throw new SaslAuthenticationError(SaslErrorCondition.CredentialsExpired)
                            case AuthenticationResult.AccountDisabled => throw new SaslAuthenticationError(SaslErrorCondition.AccountDisabled)
                            case AuthenticationResult.MalformedRequest => throw new SaslAuthenticationError(SaslErrorCondition.MalformedRequest)
                            case _ => throw new SaslAuthenticationError(SaslErrorCondition.NotAuthorized)
                        }
                    }
                    case _ => throw new SaslAuthenticationError(SaslErrorCondition.InvalidMechanism)
                }
            }
            catch
            {
                case e:SaslAuthenticationError => send(SaslError(e.reason))
                case e => throw e
            }
        }

        private def handleStanza(stanza:Stanza)
        {
            val normalizedStanza = if (stanza.from.isEmpty && this.jid.isDefined) Stanza.withFrom(stanza, this.jid.get) else stanza
            normalizedStanza match
            {
                case iq:IQ => handleIQ(iq, this.delegate.get.onStanza)
                case _ => this.delegate.get.onStanza(normalizedStanza)
            }
        }

        private def handleIQ(iq:IQ, next:(Stanza)=>Unit)
        {
            iq match
            {
                // http://xmpp.org/extensions/xep-0078.html
                case get @ Get(_, _, _, Some(request:auth.AuthenticationRequest)) =>
                {
                    state = 101
                    send(get.result(Some(auth.AuthenticationRequest("", "" , Some(""), None))))
                }
                // http://xmpp.org/extensions/xep-0078.html
                case set @ protocol.iq.Set(_, _, _, Some(request:auth.AuthenticationRequest)) =>
                {
                    state = 102
                    // not sure if this is the right place to do the JId binding under 0078
                    _jid = Some(JID(request.username, domain, request.resource))
                    this.delegate.get.authenticate(new AuthenticationRequest(request.username, request.password.getOrElse(""))) match
                    {
                        case AuthenticationResult.Success => send(set.result()); this.delegate.get.onOnline(this.jid.get)
                        case AuthenticationResult.NotAuthorized => send(set.error(StanzaErrorCondition.NotAuthorized, Some("Invalid username or password")))
                        case AuthenticationResult.CredentialsExpired => send(set.error(StanzaErrorCondition.NotAuthorized, Some("Credential Expired")))
                        case AuthenticationResult.AccountDisabled => send(set.error(StanzaErrorCondition.NotAuthorized, Some("Account Disabled")))
                        case _ => send(set.error(StanzaErrorCondition.UndefinedCondition, Some("Unknown authentication error")))
                    }
                }
                // xep-???
                case set @ protocol.iq.Set(_, _, _, Some(request:bind.BindRequest)) =>
                {
                    state = 3
                    _jid = Some(JID(_username.getOrElse(""), domain, request.resource.getOrElse(id.toString)))
                    // TODO: add hooks for subclasses to control binding behavior
                    send(set.result(Some(bind.BindResult(this.jid.get))))
                    this.delegate.get.onOnline(this.jid.get)
                }
                case set @ protocol.iq.Set(_, _, _, Some(request:bind.UnbindRequest)) =>
                {
                    // TODO: add hooks for subclasses to control unbinding behavior
                    send(set.result(Some(session.Session())))
                }
                // xep-???
                case set @ protocol.iq.Set(_, _, _, Some(request:session.Session)) =>
                {
                    state = 4
                    send(set.result(Some(session.Session())))
                }
                // http://xmpp.org/extensions/xep-0077.html
                case get @ Get(_, _, _, Some(request:register.RegistrationRequest)) =>
                {
                    // TODO: add hooks for subclasses to control registration behavior
                    send(get.result(Some(register.RegistrationRequest("", "" , "")) ))
                }
                // http://xmpp.org/extensions/xep-0077.html
                case set @ protocol.iq.Set(_, _, _, Some(request:register.RegistrationRequest)) =>
                {
                    // TODO: add hooks for subclasses to control registration behavior
                    this.delegate.get.register(new RegistrationRequest(request.username, request.password, request.email)) match
                    {
                        case RegistrationResult.Success => send(set.result())
                        case RegistrationResult.NotAcceptable => send(set.error(StanzaErrorCondition.NotAcceptable, Some("Request bot acceptable")))
                        case RegistrationResult.Conflict => send(set.error(StanzaErrorCondition.Conflict, Some("Username conflict")))
                        case RegistrationResult.NotImplemented => send(set.error(StanzaErrorCondition.NotImplemented, Some("Registration not supported")))
                        case RegistrationResult.Unknown => send(set.error(StanzaErrorCondition.UndefinedCondition, Some("Unknown registration error")))
                    }
                }
                case _ => next(iq)
            }
        }

        private def handleStreamError(error:StreamError)
        {
            // FIXME: decide what needs to be done here
            logger.error("stream error on xmpp connection %s %s".format(this.id, error))
        }

        private class SaslAuthenticationError(val reason:SaslErrorCondition.Value) extends Exception
    }
}

trait ClientConnection
{
    val id:Long
    def jid:Option[JID]

    var delegate:Option[ClientConnectionDelegate] = None

    def send(packet:Packet)

    def close()
}

trait ClientConnectionDelegate
{
    def authenticate(request:AuthenticationRequest):AuthenticationResult.Value = AuthenticationResult.Unknown
    def register(request:RegistrationRequest):RegistrationResult.Value = RegistrationResult.Unknown
    def onStanza(stanza:Stanza):Unit = Unit
    def onOnline(jid:JID):Unit = Unit
    def onOffline():Unit = Unit
    def onError(e:Throwable):Unit = Unit
}

class AuthenticationRequest(val username:String, val password:String)

object AuthenticationResult extends Enumeration
{
    type result = Value
    val Success, NotAuthorized, CredentialsExpired, AccountDisabled, MalformedRequest, Unknown = Value
}

class RegistrationRequest(val username:String, val password:String, val email:String)

object RegistrationResult extends Enumeration
{
    type result = Value
    val Success, NotImplemented, Conflict, NotAcceptable, Unknown = Value
}