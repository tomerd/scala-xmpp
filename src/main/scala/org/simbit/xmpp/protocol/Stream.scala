package org.simbit.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		object StreamHead 
		{
			protected[protocol] val tag = "<stream:stream"
			
			def qualifies(string:String):Boolean = string.indexOf(tag) >= 0
			
			def apply(string:String):StreamHead =
			{
				val xml = XML.loadString(string + StreamTail.tag)
				var attributes = xml.attributes.map( attribute => Tuple2(attribute.key, attribute.value(0).text) )
				apply(xml.scope.uri, attributes)
			}
			
			def apply(namespace:String, attributes:Iterable[Tuple2[String, String]]):StreamHead = new StreamHead(namespace, attributes)
		}
		
		class StreamHead(val namespace:String, val attributes:Iterable[Tuple2[String, String]]) extends Packet
		{
			def findAttribute(name:String):Option[String] = 
			{
				attributes.find ( attribute => name == attribute._1 ) match
				{
					case Some(tuple) => Some(tuple._2)
					case None => None
				}
			}
			
			override def toString:String = StreamHead.tag + " xmlns:stream='http://etherx.jabber.org/streams' xmlns='" + namespace + "'" + attributes.map( attribute => attribute._1 + "='" + attribute._2  + "'" ).mkString(" ", " ", ">")
		}
		
		object StreamTail
		{
			protected[protocol] val tag = "</stream:stream>"
			
			def qualifies(string:String):Boolean = string == tag
			
			def apply():StreamTail = new StreamTail()
		}
		
		class StreamTail extends Packet
		{
			override def toString:String = StreamTail.tag
		}
		
	}
}