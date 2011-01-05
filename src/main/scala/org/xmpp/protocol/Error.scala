package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._
				
		final object Error
		{
			val NAMESPACE:String = "urn:ietf:params:xml:ns:xmpp-stanzas"
				
			def apply(xml:Node):Error = new Error(xml)	
				
			def apply(condition:ErrorCondition.Value, description:Option[String]=None, otherConditions:Option[Seq[String]]=None):Error =
			{
				import org.xmpp.protocol.ErrorCondition._
				// TODO: test this
				val children = mutable.ListBuffer[Node]()
				children += Elem(null, condition.toString, new UnprefixedAttribute("xmlns", Text(NAMESPACE), Null), TopScope)
				if (!otherConditions.isEmpty) otherConditions.foreach( condition => { children += Elem(null, condition.toString, Null, TopScope) } )
				if (!description.isEmpty) children += <text xmlns={ NAMESPACE } xml:lang="en">{ description.get }</text>
				var attributes:MetaData = new UnprefixedAttribute("type", Text(condition.kind.toString), Null)
						
				return new Error(Elem(null, "error", attributes, TopScope, children:_*))
			}
		}
		
		protected final class Error(xml:Node) extends XmlWrapper(xml)
		{	
			parse
			
			// getters
			private var _kind:Option[ErrorType.Value] = None
			private def kind:Option[ErrorType.Value] = _kind
			
			private var _condition:Option[ErrorCondition.Value] = None
			private def condition:Option[ErrorCondition.Value] = _condition
			
			private var _description:Option[String] = None
			private def description:Option[String] = _description
			
			private var _otherConditions:Option[Seq[String]] = None
			private def otherConditions:Option[Seq[String]] = _otherConditions
			
			protected def parse
			{				
				val kind = (this.xml \ "@type").text
				_kind = if (kind.isEmpty) None else Some(ErrorType.withName(kind))
				
				_condition = xml.child.find((child) => child.namespace.equals(Error.NAMESPACE) && !child.label.equals("text")) match
				{
					case Some(node) => 
					{
						ErrorCondition.fromString(node.label) match
						{
							case Some(error) => Some(error)
							case None => None
						}
					}
					case None =>
					{
						ErrorCondition.fromLegacyCode((this.xml \ "@code").text.toInt) match
						{
							case Some(error) => Some(error)
							case None => None
						}
					}
				} 
									
				val description = (this.xml \ "text").text
				_description = if (description.isEmpty) None else Some(description)
				
				val conditions = mutable.ListBuffer[String]()			
				xml.child.filter((child) => Error.NAMESPACE != child.namespace).foreach(child => { conditions += child.label })
				_otherConditions = if (conditions.isEmpty) None else Some(conditions)
			}		
		}

		final object ErrorCondition extends Enumeration
		{
			type condition = Value
			
			val BadRequest = Condition("bad-request", ErrorType.Modify, 400)
			val Conflict = Condition("conflict", ErrorType.Cancel, 409)
			val NotImplemented = Condition("feature-not-implemented", ErrorType.Cancel, 501)
			val Forbidden = Condition("forbidden", ErrorType.Auth, 403)
			val Gone = Condition("gone", ErrorType.Modify, 302)
			val InternalServerError = Condition("internal-server-error", ErrorType.Wait, 500)
			val NotFound = Condition("item-not-found", ErrorType.Cancel, 404)
			val BadJid = Condition("jid-malformed", ErrorType.Modify, 400)
			val NotAcceptable = Condition("not-acceptable", ErrorType.Modify, 406)
			val NotAllowed = Condition("not-allowed", ErrorType.Cancel, 405)
			val NotAuthorized = Condition("not-authorized", ErrorType.Auth, 401)
			val PaymentRequired = Condition("payment-required", ErrorType.Auth, 402)
			val RecipientUnavailable = Condition("recipient-unavailable", ErrorType.Wait, 404)
			val Redirect = Condition("redirect", ErrorType.Modify, 302)
			val RegistrationRequired = Condition("registration-required", ErrorType.Auth, 407)
			val RemoteServerNotFound = Condition("remote_server_not_found", ErrorType.Cancel, 404)
			val RemoteServerTimeout = Condition("remote-server-timeout", ErrorType.Wait, 504)		
			val ResourceConstraint = Condition("resource-constraint", ErrorType.Wait, 500)
			val ServiceUnavailable = Condition("service-unavailable", ErrorType.Cancel, 503)
			val SubscriptionRequired = Condition("subscription-required", ErrorType.Auth, 407)		
			val UndefinedCondition = Condition("undefined-condition", ErrorType.Cancel, 500) // default behavior is not by specification
			val UnexpectedRequest = Condition("unexpected-request", ErrorType.Wait, 400)			
			val Unknown = Condition("unknown", ErrorType.Cancel, 0) // internal use, outside of spec!
			
			def fromString(string:String):Option[ErrorCondition.Value] = values.find((value) => value.name.equals(string.toLowerCase))
			def fromLegacyCode(code:Int):Option[ErrorCondition.Value] = values.find((value) => value.legacyCode.equals(code))
			
			case class Condition(name:String, val kind:ErrorType.Value, val legacyCode:Int) extends Val(name)
			
			implicit def valueToCondition(value:Value):Condition = value.asInstanceOf[Condition]
		}
				
		final object ErrorType extends Enumeration
		{
			type action = Value

			val Cancel = Value("cancel")
			val Continue = Value("continue")
			val Modify = Value("modify")
			val Auth = Value("auth")
			val Wait = Value("wait")
		}
	
	}
}

