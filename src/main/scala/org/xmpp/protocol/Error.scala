package org.xmpp
{
	package protocol
	{
		import scala.xml._
		
		final object Error
		{
			val ERROR_NAMESPACE:String = "urn:ietf:params:xml:ns:xmpp-stanzas"
		}
		
		// TODO: test this
		final class Error(literal:Node)
		{
			def xml:Node = literal
			
			def kind:ErrorType.Value = ErrorType.withName((this.xml \ "@type").text)
			
			def condition:XmppError = 
			{
				// TODO: test this
				xml.child.find((child) => child.namespace.equals(Error.ERROR_NAMESPACE) && !child.label.equals("text")) match
				{
					case Some(node) => 
					{
						XmppError.fromString(node.label) match
						{
							case Some(error) => error
							case None => new Unknown
						}
					}
					case None =>
					{
						XmppError.fromCode((this.xml \ "@code").text.toInt) match
						{
							case Some(error) => error
							case None => new Unknown
						}
					}
				}
			}
			
			def text:String = (this.xml \ "text").text
		}
		
		// TODO: test this
		object XmppError
		{
			val ALL = List[XmppError]() //BadRequest,Conflict,NotImplemented,Forbidden,Gone,InternalServerError,NotFound,BadJid,NotAcceptable,NotAllowed,NotAuthorized,PaymentRequired,RecipientUnavailable,Redirect,RegistrationRequired,RemoteServerNotFound,RemoteServerTimeout,ResourceConstraint,ServiceUnavailable,SubscriptionRequired,UndefinedCondition,UnexpectedRequest)
			
			def fromString(string:String):Option[XmppError] = ALL.find((error) => error.name.equals(string.toLowerCase))
			def fromCode(code:Int):Option[XmppError] = ALL.find((error) => error.code.equals(code))
		}
		
		// TODO: test this
		// TODO: validate this covers the entire spec correctly
		class XmppError(val name:String, val code:Int, val action:ErrorType.Value)		
		case class Unknown extends XmppError("unknown", 0, ErrorType.Cancel)
		case class BadRequest extends XmppError("bad-request", 400, ErrorType.Modify)
		case class Conflict extends XmppError("conflict", 409, ErrorType.Cancel)
		case class NotImplemented extends XmppError("feature-not-implemented", 501, ErrorType.Cancel)
		case class Forbidden extends XmppError("forbidden", 403, ErrorType.Auth)
		case class Gone extends XmppError("gone", 302, ErrorType.Modify)
		case class InternalServerError extends XmppError("internal-server-error", 500, ErrorType.Wait)
		case class NotFound extends XmppError("item-not-found", 404, ErrorType.Cancel)
		case class BadJid extends XmppError("jid-malformed", 400, ErrorType.Modify)
		case class NotAcceptable extends XmppError("not-acceptable", 406, ErrorType.Modify)
		case class NotAllowed extends XmppError("not-allowed", 405, ErrorType.Cancel)
		case class NotAuthorized extends XmppError("not-authorized", 401, ErrorType.Auth)
		case class PaymentRequired extends XmppError("payment-required", 402, ErrorType.Auth)
		case class RecipientUnavailable extends XmppError("recipient-unavailable", 404, ErrorType.Wait)
		case class Redirect extends XmppError("redirect", 302, ErrorType.Modify)
		case class RegistrationRequired extends XmppError("registration-required", 407, ErrorType.Auth)
		case class RemoteServerNotFound extends XmppError("remote_server_not_found", 404, ErrorType.Cancel)
		case class RemoteServerTimeout extends XmppError("remote-server-timeout", 504, ErrorType.Wait)		
		case class ResourceConstraint extends XmppError("resource-constraint", 500, ErrorType.Wait)
		case class ServiceUnavailable extends XmppError("service-unavailable", 503, ErrorType.Cancel)
		case class SubscriptionRequired extends XmppError("subscription-required", 407, ErrorType.Auth)		
		case class UndefinedCondition extends XmppError("undefined-condition", 500, ErrorType.Cancel) // default behavior is not by specification
		case class UnexpectedRequest extends XmppError("unexpected-request", 400, ErrorType.Wait)
		
		object ErrorType extends Enumeration
		{
			type action = Value

			val Unknown = Value("unknonw")
			val Cancel = Value("cancel")
			val Continue = Value("continue")
			val Modify = Value("modify")
			val Auth = Value("auth")
			val Wait = Value("wait")
		}
	
	}
}

