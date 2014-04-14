package com.mishlabs.xmpp
package network

import scala.collection._
import scala.xml._
import scala.xml.pull._

import org.jboss.netty.buffer.ChannelBuffers

import com.twitter.naggati._
import com.twitter.naggati.Stages._

import protocol._

protected object XmppCodec
{
    def apply() = new Codec(decode, encode)
    def apply(bytesReadCounter:Int => Unit, bytesWrittenCounter:Int => Unit) = new Codec(decode, encode, bytesReadCounter, bytesWrittenCounter)

    val encode = new Encoder[Packet]
    {
        def encode(packet:Packet) =
        {
            val bytes = packet.toString.getBytes
            val buffer = ChannelBuffers.buffer(bytes.length)
            buffer.writeBytes(bytes)
            Some(buffer)
        }
    }

    /*
    val encode:PartialFunction[Any, ChannelBuffer] =
    {
        case packet:Packet =>
        {
            val bytes = packet.toString.getBytes
            val buffer = ChannelBuffers.buffer(bytes.length)
            buffer.writeBytes(bytes)
            buffer
        }
        case text:String =>
        {
            val bytes = text.getBytes
            val buffer = ChannelBuffers.buffer(bytes.length)
            buffer.writeBytes(bytes)
            buffer
        }
    }
    */

    val decode = parse("")

    def parse(buffer:String):Stage =
    {
        //readLine(true, "UTF-8") { line =>
            //val input = buffer + line

        readBytes(1) { bytes =>

            val char = new String(bytes, "UTF-8")
            // TODO: find a better way to do this
            if (("\r" == char || "\n" == char) && (buffer.isEmpty || buffer.endsWith(">")))
            {
                parse(buffer)
            }
            else
            {
                val input = buffer + char

                if (!input.endsWith(">"))
                {
                    // assume partial packet. buffer till rest of required packets arrive
                    parse(input)
                }
                else if (StreamHead.qualifies(input))
                {
                    Emit(StreamHead(input))
                }
                else if (StreamTail.qualifies(input))
                {
                    Emit(StreamTail())
                }
                else
                {
                    try
                    {
                        parseXml(input) match
                        {
                            case Some(xmls) =>
                            {
                                val ouput = xmls.map( xml =>
                                {
                                    xml.namespace match
                                    {
                                        case Tls.namespace => xml.label match
                                        {
                                            case StartTls.tag => StartTls(xml)
                                            case TlsProceed.tag => TlsProceed(xml)
                                            case TlsFailure.tag => TlsFailure(xml)
                                            case _ => throw new Exception("unknown tls packet %s".format(xml.label))
                                        }
                                        case Sasl.namespace => xml.label match
                                        {
                                            case SaslAuth.tag => SaslAuth(xml)
                                            case SaslSuccess.tag => SaslSuccess(xml)
                                            case SaslAbort.tag => SaslAbort(xml)
                                            case SaslError.tag => SaslError(xml)
                                            case _ => throw new Exception("unknown sasl packet %s".format(xml.label))
                                        }
                                        case _ => xml.label match
                                        {
                                            case Features.tag => Features(xml)
                                            case Handshake.tag => Handshake(xml)
                                            case StreamError.tag => StreamError(xml)
                                            case _ => Stanza(xml)
                                        }
                                    }
                                })

                                Emit(if (ouput.length > 1) ouput else ouput(0))
                            }
                            case None =>
                            {
                                // assume partial packet. buffer till rest of required packets arrive
                                parse(input)
                            }
                        }
                    }
                    catch
                    {
                        case e => throw new ProtocolError("internal error decoding packet %s".format(input), e)
                    }
                }
            }
        }
    }

    def parseXml(input:String):Option[Seq[Node]] =
    {
        var level = 0
        val children = mutable.HashMap[Int, mutable.ListBuffer[Node]]()
        val attributes = mutable.HashMap[Int, MetaData]()
        val scope = mutable.HashMap[Int, NamespaceBinding]()
        val nodes = mutable.ListBuffer[Node]()

        try
        {
            // using a customized version of XMLEventReadr as it is buggy, see
            // http://scala-programming-language.1934581.n4.nabble.com/OutOfMemoryError-when-using-XMLEventReader-td2341263.html
            //  should be fixed in scala 2.9, need to test when it is released
            val tokenizer = new XMLEventReaderEx(scala.io.Source.fromString(input))
            tokenizer.foreach( token =>
            {
                token match
                {
                    case tag:EvText => children(level) += new Text(tag.text)
                    case tag:EvProcInstr => children(level) += new ProcInstr(tag.target, tag.text)
                    case tag:EvComment => children(level) += new Comment(tag.text)
                    case tag:EvEntityRef => children(level) += new EntityRef(tag.entity)
                    case tag:EvElemStart =>
                    {
                        level += 1
                        if (!attributes.contains(level)) attributes += level -> tag.attrs else attributes(level) = tag.attrs
                        if (!scope.contains(level)) scope += level -> tag.scope else scope(level) = tag.scope
                        if (!children.contains(level)) children += level -> new mutable.ListBuffer[Node]() else children(level) = new mutable.ListBuffer[Node]()
                    }
                    case tag:EvElemEnd =>
                    {
                        val node = Elem(tag.pre, tag.label, attributes(level), scope(level), children(level):_*)

                        level -= 1
                        if (0 == level)
                        {
                            nodes += node
                        }
                        else
                        {
                            children(level) += node
                        }
                    }
                }
            })

            return if (nodes.length > 0) Some(nodes) else None
        }
        catch
        {
            // TODO: would be nice to handle bad vs. partial xml, only the latter is important to us (for buffering)
            case e:parsing.FatalError => None
            case e => throw e
        }
    }
}