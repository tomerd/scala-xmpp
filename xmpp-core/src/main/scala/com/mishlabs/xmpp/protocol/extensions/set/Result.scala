package com.mishlabs.xmpp
package protocol
package extensions
package set

import scala.collection._
import scala.xml._

import Protocol._

object Result extends ExtensionBuilder[Result]
{
    val tag = Request.tag
    val namespace = Request.namespace

    def apply[T](total:Int, first:Option[Tuple2[T, Int]], last:Option[T]):Result =
    {
        val children = mutable.ListBuffer[Node]()
        children += <count>{ total }</count>
        if (!first.isEmpty) children += <first index={ first.get._2.toString }>{ first.get._1.toString }</first>
        if (!last.isEmpty) children += <last>{ last.toString }</last>
        apply(build(children))
    }

    def apply(xml:Node):Result = new Result(xml)
}

class Result (xml:Node) extends Extension(xml)
{
}



