package org.simbit.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object StreamError
		{
			val tag = "error"				
			val namespace:String = "urn:ietf:params:xml:ns:xmpp-streams"
			
			def apply(condition:StreamErrorCondition.Value, description:Option[String]=None, otherConditions:Option[Seq[String]]=None):StreamError =
			{				
				val children = mutable.ListBuffer[Node]()
				// TODO: test the namespace
				children += Elem(null, condition.toString, Null, new NamespaceBinding(null, namespace, TopScope))				
				if (!otherConditions.isEmpty) otherConditions.foreach( condition => { children += Elem(null, condition.toString, Null, TopScope) } )
				if (!description.isEmpty) children += <text xmlns={ namespace } xml:lang="en">{ description.get }</text>
						
				return new StreamError(Elem(null, tag, Null, TopScope, children:_*))
			}
			
			def apply(xml:Node):StreamError = new StreamError(xml)
		}
		
		class StreamError(xml:Node) extends XmlWrapper(xml)
		{			
			val condition:StreamErrorCondition.Value = 
			{
				this.xml.child.find( (child) => StreamError.namespace == child.namespace && "text" != child.label ) match
				{
					case Some(node) => StreamErrorCondition.withName(node.label) 
					case _ => throw new Exception("unknown error condition " + this.xml)
				}
			}
			
			val description:Option[String] = (this.xml \ "text").text
			
			val otherConditions:Option[Seq[String]] =
			{
				val conditions = mutable.ListBuffer[String]()
				xml.child.filter( (child) => StreamError.namespace != child.namespace).foreach(child => { conditions += child.label } )
				if (conditions.isEmpty) None else Some(conditions)
			}
		}
			
		object StreamErrorCondition extends Enumeration
		{
			type condition = Value
			
			val BadFormat = Value("bad-format")
			val BadNamespacePrefix = Value("bad-namespace-prefix")
			val Conflict = Value("conflict")
			val ConnectionTimeout = Value("connection-timeout")			
			val HostGone = Value("host-gone")
			val HostUnknown = Value("host-unknown")
			val ImproperAddressing = Value("improper-addressing")			
			val InternalServerError = Value("internal-server-error")
			val BadFrom = Value("invalid-from")
			val BadId = Value("invalid-id")
			val BadXml = Value("invalid-xml")
			val NotAuthorized = Value("not-authorized") 
			val PolicyViolation = Value("policy-violation")
			val RemoteConnectionFailed = Value("remote-connection-failed")
			val ResourceConstraint = Value("resource-constraint")
			val RestrictedXml = Value("restricted-xml")
			val SeeOtherHost = Value("see-other-host")
			val SystemShutdown = Value("system-shutdown")
			val UndefinedValue = Value("undefined-Value")
			val UnsupportedEncoding = Value("unsupported-encoding")
			val UnsupportedStanzaType = Value("unsupported-stanza-type")
      		val UnsupportedVersion = Value("unsupported-version")
      		val XmlNotWellFormed = Value("xml-not-well-formed")
			val Unknown = Value("unknown") // internal use, outside of spec!					
		}
	}
}