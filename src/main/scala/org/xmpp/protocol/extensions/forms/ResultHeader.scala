package org.xmpp
{
	package protocol.extensions.forms
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.extensions.forms.fields._
		
		import org.xmpp.protocol.Protocol._
		
		object ResultHeader 
		{
			val tag = "reported"
			
			def apply(fields:Seq[Field]):ResultHeader = apply(build(fields))
 			
			def apply(xml:Node):ResultHeader = new ResultHeader(xml)
			
			def build(fields:Seq[Field]):Node =
			{
				Elem(null, tag, Null, TopScope, fields:_*)
			}
		}
		
		class ResultHeader(xml:Node) extends XmlWrapper(xml)
		{
		}
		
	}
}
