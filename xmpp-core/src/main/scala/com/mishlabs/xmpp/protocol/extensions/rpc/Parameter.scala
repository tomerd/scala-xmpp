package com.mishlabs.xmpp
package protocol
package extensions
package rpc

import scala.xml._

object Parameter
{
    val tag = "param"

    def apply(value:Object):Parameter = apply(buildFullNode(buildValueNode(value)))

    private def buildValueNode(value:Object):Node =
    {
        castToSupportedType(value) match
        {
            case null => buildDataTypeNode("nil", "")
            case value:java.lang.String => buildDataTypeNode("string", value)
            case value:java.lang.Integer => buildDataTypeNode("int", value.toString)
            case value:java.lang.Double => buildDataTypeNode("double", value.toString)
            case value:java.lang.Boolean => buildDataTypeNode("boolean", value.toString)
            case value:java.util.Date => buildDataTypeNode("dateTime.iso8601", dateToIso8601(value))
            case array:Array[Object] =>
            {
                val children = scala.collection.mutable.ListBuffer[Node]()
                array.foreach ( item => children += buildValueNode(item) )
                val dataNode = Elem(null, "data", Null, TopScope, children:_*)
                buildDataTypeNode("array", dataNode)
            }
            case map:java.util.Map[String, Object] =>
            {
                val members = scala.collection.mutable.ListBuffer[Node]()
                val iterator = map.entrySet.iterator
                while (iterator.hasNext)
                {
                    val entry = iterator.next
                    members += <member><name>{ entry.getKey }</name>{ buildValueNode(entry.getValue) }</member>
                }
                buildDataTypeNode("struct", members)
            }

            case _ => throw new Exception("cant or dont know how to cast " + value + " to an xml-rpc parameter")
        }
    }

    private def buildDataTypeNode(dataType:String, value:String):Node = buildDataTypeNode(dataType, Text(value))

    private def buildDataTypeNode(dataType:String, values:Seq[Node]):Node = Elem(null, dataType, Null, TopScope, values:_*)

    private def buildFullNode(dataTypeNode:Node):Node = Elem(null, tag, Null, TopScope, <value>{ dataTypeNode }</value>)

    def apply(xml:Node) = new Parameter(xml)

    private def castToSupportedType(value:Any):Object =
    {
        value match
        {
            case null => null
            case value:java.lang.String => value
            case value:java.lang.Integer => value
            case value:Int => value.asInstanceOf[java.lang.Integer]
            case value:java.lang.Double => value
            case value:Double => value.asInstanceOf[java.lang.Double]
            case value:java.lang.Boolean => value
            case value:Boolean => value.asInstanceOf[java.lang.Boolean]
            case value:java.util.Date => value
            case array:Array[Object] =>
            {
                val jarray = new Array[Object](array.length)
                for (index <- 0 until array.length)
                {
                    jarray(index) = castToSupportedType(array(index))
                }
                jarray
            }
            case seq:scala.collection.Seq[Object] => seq.map( item => castToSupportedType(item) ).toArray
            case map:java.util.Map[String, Object] =>
            {
                val jmap = new java.util.LinkedHashMap[String, Object]()
                val iterator = map.entrySet.iterator
                while (iterator.hasNext)
                {
                    val entry = iterator.next
                    val value = castToSupportedType(entry.getValue)
                    jmap.put(entry.getKey, value)
                }
                jmap
            }
            case map:scala.collection.Map[String, Object] =>
            {
                val jmap = new java.util.LinkedHashMap[String, Object]()
                map.keySet.foreach ( key =>
                {
                    val value = castToSupportedType(map(key))
                    jmap.put(key, value)
                })
                jmap
            }
            // TODO, finish these (base64, extensions, etc)
            case _ => throw new Exception("cant or dont know how to cast " + value + " to an xml-rpc parameter")
        }
    }

    // XMLRPC ISO8601 format = 19980717T14:08:55
    private def dateToIso8601(date:java.util.Date):String =
    {
        val builder = new StringBuilder(17)
        builder.append(pad(date.getYear+1900, 4))
        builder.append(pad(date.getMonth+1))
        builder.append(pad(date.getDate))
        builder.append("T")
        builder.append(pad(date.getHours))
        builder.append(":")
        builder.append(pad(date.getMinutes))
        builder.append(":")
        builder.append(pad(date.getSeconds))
        return builder.toString
    }

    private def pad(number:Int, size:Int=2):String = String.format("%0" + size + "d", number.asInstanceOf[java.lang.Integer])

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
            // TODO, finish these (base64, extensions, etc)
            case "nil" => null
            case "string" => stringValue
            case "boolean" => "true" == stringValue.toLowerCase || "yes" == stringValue.toLowerCase || "1" == stringValue
            case "i4" => stringValue.toInt
            case "int" =>  stringValue.toInt
            case "double" => stringValue.toDouble
            case "dateTime.iso8601" => iso8601ToDate(stringValue)
            case "base64" => stringValue
            case "struct" =>
            {
                val map = new scala.collection.mutable.ListMap[String, Any]
                (dataNode \ "member").foreach ( node => map += (node \ "name").text -> parseValue((node \ "value")(0)))
                map
            }
            case "array" => (dataNode \ "data")(0).child.filter( child => !child.text.trim.isEmpty ).map( node => parseValue(node) )
            case _ => throw new Exception("unknown data type " + dataType + " please see XML-RPC spec for supported datatypes")
        }
    }

    // XMLRPC ISO8601 format = 19980717T14:08:55
    private def iso8601ToDate(value:String):java.util.Date =
    {
        try
        {
            if (value.length() != 17) throw new Exception("invalid length, expected 17 characters")
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