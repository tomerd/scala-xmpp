package org.simbit.xmpp
{
	package protocol.extensions.forms
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.extensions.forms.fields._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		object ResultItem
		{
			val tag = "item"
			
			def apply(fields:Seq[Field]):ResultItem = apply(build(fields))
 			
			def apply(xml:Node):ResultItem = new ResultItem(xml)
			
			def build(fields:Seq[Field]):Node =
			{
				Elem(null, tag, Null, TopScope, fields:_*)
			}
		}
		
		class ResultItem(xml:Node) extends XmlWrapper(xml)
		{
		}
		
	}
}
