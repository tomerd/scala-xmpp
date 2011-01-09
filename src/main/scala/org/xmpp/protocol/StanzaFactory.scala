package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.iq.IQFactory
		import org.xmpp.protocol.presence.PresenceFactory
		import org.xmpp.protocol.message.MessageFactory
				
		trait ExtendedStanzaBuilder[T <: Stanza]
		{
			val kind:String
			val name:String
			val namespace:String
			def apply(xml:Node):T
		}
				
		trait StanzaFactory[T <: Stanza]
		{
			private val builders = mutable.ListBuffer[ExtendedStanzaBuilder[_]]()
				
			def create(xml:Node):T
			
			final def registerExtension[E <: T](builder:ExtendedStanzaBuilder[E])
			{
				builders += builder
			}
			
			final def createExtended(xml:Node):Option[T] =
			{
				if (0 == builders.length) return None
				
				val kind:String = (xml \ "@type").text
				val iterator = xml.child.iterator
				while (iterator.hasNext)
				{
					val node = iterator.next
					findBuilder(kind, node.label, node.namespace) match
					{
						case Some(builder) => return Some(builder.apply(xml).asInstanceOf[T])
						case None => // continue
					}
				}
				
				return None
			}
			
			private def findBuilder(kind:String, name:String, namesapce:String):Option[ExtendedStanzaBuilder[_]] = 
			{
				builders.find( builder => kind == builder.kind && name == builder.name && namesapce == builder.namespace )
			}
			
		}
		
		object StanzaFactory
		{		
			def create(xml:Node):Stanza =
			{			
				val factory = xml match
				{
					case <iq>{ _* }</iq> => IQFactory
					case <presence>{ _* }</presence> => PresenceFactory
					case <message>{ _* }</message> => MessageFactory
					case _ => throw new Exception("unknown stanza type, expected iq, presence or message")
				}
				
				factory.createExtended(xml) match
				{
					case Some(stanza) => stanza
					case None => factory.create(xml)
				}
			}
		}
	}
}