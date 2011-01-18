package org.xmpp
{
	package protocol.extensions.set
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		object Result extends ExtensionBuilder[Result]
		{
			val name = Request.name
			val namespace = Request.namespace
			
			def apply[T](first:T, last:T, startIndex:Int, total:Int):Result = 
			{
				val children = mutable.ListBuffer[Node]()
				children += <first index={ startIndex.toString }>{ first.toString }</first>
				children += <last>{ last.toString }</last>
				children += <count>{ total }</count>
				apply(build(children))
			}
			
			def apply(xml:Node):Result = new Result(xml)
		}
		
		class Result (xml:Node) extends Extension(xml)
		{
		}
	}
}



