package org.simbit.xmpp
{
	package protocol.extensions.rpc
	{		
		import scala.xml._
		import scala.collection._
		
		import org.simbit.xmpp.protocol._
		
		// TODO: implement xml-rpc datatype serializing / deserialzing instead of using simple string
		
		object Parameter
		{
			val tag = "param"
			
			def apply(value:String):Parameter = apply("string", value)
			def apply(value:Int):Parameter = apply("int", value.toString)
			def apply(value:Double):Parameter = apply("double", value.toString)
			def apply(value:Boolean):Parameter = apply("boolean", value.toString)
			
			def apply(dataType:String, value:String):Parameter = 
			{
				val dataTypeNode = Elem(null, dataType, Null, TopScope, Text(value))
				val valueNode = Elem(null, "value", Null, TopScope, dataTypeNode)
				apply(Builder.build(Elem(null, tag, Null, TopScope, valueNode)))
			}
			
			def apply(xml:Node) = new Parameter(xml)
		}
		
		class Parameter(xml:Node) extends XmlWrapper(xml)
		{
			private val valueNode = (xml \ "value")(0)
			
			val dataType:String = this.valueNode.child(0).label
			
			def value:Any = parseValue(this.valueNode)
			
			private def parseValue(node:Node):Any = 
			{
				val dataNode = node.child.find( child => !child.text.trim.isEmpty ).getOrElse(return null)
				val dataType:String = dataNode.label
				val stringValue = dataNode.text.trim
				
				dataType match
				{
					case "nil" => null
					case "string" => stringValue
					case "boolean" => "true" == stringValue.toLowerCase || "yes" == stringValue.toLowerCase || "1" == stringValue
					case "i4" => stringValue.toInt
					case "int" =>  stringValue.toInt
					case "double" => stringValue.toDouble
					case "dateTime.iso8601" => parseIso8601Date(stringValue)
					case "base64" => stringValue // TODO: should this be wrapped with a special node?
					case "struct" => 
					{
						val map:mutable.Map[String, Any] = new mutable.ListMap[String, Any]
						(dataNode \ "member").foreach ( node => map += (node \ "name").text -> parseValue((node \ "value")(0)))
						map
					}
					case "array" => (dataNode \ "data")(0).child.filter( child => !child.text.trim.isEmpty ).map( node => parseValue(node) )
					case _ => throw new Exception("unknown data type " + dataType + " please see XML-RPC spec for supported datatypes")
				}
			}
			
			// XMLRPC ISO8601 format = 19980717T14:08:55
			private def parseIso8601Date(value:String):java.util.Date =
			{
				try 
				{
					if (value.length() != 17) throw new Exception("invalid length, expected 17 charchters")
					val date = new java.util.Date()
					date.setYear(value.substring(0, 4).toInt-1900)
					date.setMonth(value.substring(4, 6).toInt-1)
					date.setDate(value.substring(6, 8).toInt)
					date.setHours(value.substring(9, 11).toInt)
					date.setMinutes(value.substring(12, 14).toInt)
					date.setSeconds(value.substring(15).toInt)					
					return date
				} 
				catch 
				{
					case e:Exception => throw new Exception("invalid format "+ value + ", expected XMLRPC ISO8601")
				}
			}
		}
		
	}
}