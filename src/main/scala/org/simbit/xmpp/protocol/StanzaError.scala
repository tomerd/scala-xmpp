package org.simbit.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol.Protocol._
				
		object StanzaError
		{
			val tag = "error"
			val namespace:String = "urn:ietf:params:xml:ns:xmpp-stanzas"
			
			def apply(condition:StanzaErrorCondition.Value, description:Option[String]=None, otherConditions:Option[Seq[String]]=None):StanzaError =
			{
				import org.simbit.xmpp.protocol.StanzaErrorCondition._
				
				val children = mutable.ListBuffer[Node]()
				// TODO: test the namespace
				children += Elem(null, condition.toString, Null, new NamespaceBinding(null, namespace, TopScope))				
				if (!otherConditions.isEmpty) otherConditions.foreach( condition => { children += Elem(null, condition.toString, Null, TopScope) } )
				if (!description.isEmpty) children += <text xmlns={ namespace } xml:lang="en">{ description.get }</text>
				var attributes:MetaData = new UnprefixedAttribute("type", Text(condition.action.toString), Null)
						
				return new StanzaError(Elem(null, tag, attributes, TopScope, children:_*))
			}
			
			def apply(xml:Node):StanzaError = new StanzaError(xml)
		}
		
		protected class StanzaError(xml:Node) extends XmlWrapper(xml)
		{
			val errorType:Option[StanzaErrorAction.Value] = 
			{
				val errorType = (this.xml \ "@type").text
				if (errorType.isEmpty) None else Some(StanzaErrorAction.withName(errorType))
			}
			
			val condition:StanzaErrorCondition.Value = 
			{
				this.xml.child.find( (child) => StanzaError.namespace == child.namespace && "text" != child.label ) match
				{
					case Some(node) => 
					{
						StanzaErrorCondition.fromString(node.label) match
						{
							case Some(condition) => condition
							case _ => throw new Exception("unknown error condition " + this.xml)
						}
					}
					case _ =>
					{
						StanzaErrorCondition.fromLegacyCode((this.xml \ "@code").text.toInt) match
						{
							case Some(condition) => condition
							case _ => throw new Exception("unknown error condition " + this.xml)
						}
					}
				}
			}
			
			val description:Option[String] = (this.xml \ "text").text 
			
			val otherConditions:Option[Seq[String]] =
			{
				val conditions = mutable.ListBuffer[String]()
				xml.child.filter( (child) => StanzaError.namespace != child.namespace).foreach(child => { conditions += child.label } )
				if (conditions.isEmpty) None else Some(conditions)
			}
			
		}

		object StanzaErrorCondition extends Enumeration
		{
			type condition = Value
			
			val BadRequest = Condition("bad-request", StanzaErrorAction.Modify, 400)
			val Conflict = Condition("conflict", StanzaErrorAction.Cancel, 409)
			val NotImplemented = Condition("feature-not-implemented", StanzaErrorAction.Cancel, 501)
			val Forbidden = Condition("forbidden", StanzaErrorAction.Auth, 403)
			val Gone = Condition("gone", StanzaErrorAction.Modify, 302)
			val InternalServerError = Condition("internal-server-error", StanzaErrorAction.Wait, 500)
			val NotFound = Condition("item-not-found", StanzaErrorAction.Cancel, 404)
			val BadJid = Condition("jid-malformed", StanzaErrorAction.Modify, 400)
			val NotAcceptable = Condition("not-acceptable", StanzaErrorAction.Modify, 406)
			val NotAllowed = Condition("not-allowed", StanzaErrorAction.Cancel, 405)
			val NotAuthorized = Condition("not-authorized", StanzaErrorAction.Auth, 401)
			val PaymentRequired = Condition("payment-required", StanzaErrorAction.Auth, 402)
			val RecipientUnavailable = Condition("recipient-unavailable", StanzaErrorAction.Wait, 404)
			val Redirect = Condition("redirect", StanzaErrorAction.Modify, 302)
			val RegistrationRequired = Condition("registration-required", StanzaErrorAction.Auth, 407)
			val RemoteServerNotFound = Condition("remote_server_not_found", StanzaErrorAction.Cancel, 404)
			val RemoteServerTimeout = Condition("remote-server-timeout", StanzaErrorAction.Wait, 504)		
			val ResourceConstraint = Condition("resource-constraint", StanzaErrorAction.Wait, 500)
			val ServiceUnavailable = Condition("service-unavailable", StanzaErrorAction.Cancel, 503)
			val SubscriptionRequired = Condition("subscription-required", StanzaErrorAction.Auth, 407)		
			val UndefinedCondition = Condition("undefined-condition", StanzaErrorAction.Cancel, 500) // default behavior is not by specification
			val UnexpectedRequest = Condition("unexpected-request", StanzaErrorAction.Wait, 400)			
			val Unknown = Condition("unknown", StanzaErrorAction.Cancel, 0) // internal use, outside of spec!
			
			def fromString(string:String):Option[StanzaErrorCondition.Value] = values.find((value) => value.name.equals(string.toLowerCase))
			def fromLegacyCode(code:Int):Option[StanzaErrorCondition.Value] = values.find((value) => value.legacyCode.equals(code))
			
			protected case class Condition(name:String, val action:StanzaErrorAction.Value, val legacyCode:Int) extends Val(name)
			
			implicit def valueToCondition(value:Value):Condition = value.asInstanceOf[Condition]
		}
				
		object StanzaErrorAction extends Enumeration
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

