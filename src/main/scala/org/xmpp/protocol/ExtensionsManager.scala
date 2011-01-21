package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		import org.xmpp.protocol.extensions._
		
		import org.xmpp.protocol.Protocol._
		
		trait ExtensionBuilder[T <: Extension]
		{
			val name:String
			val namespace:String
			
			//def apply(xml:Node):T
			
			def apply():T = apply(Null, Nil)
			
			def apply(children:Seq[Node]):T = apply(Null, children)
			
			def apply(attributes:MetaData):T = apply(attributes, Nil)
			
			def apply(attributes:MetaData, children:Seq[Node]):T = apply(build(attributes, children))
			
			//def apply(other:Extension, children:Seq[Node]=Nil):T = apply(build(other.xml.label, other.xml.scope.uri, other.xml.attributes, children))
			
			def apply(xml:Node):T
			
			def build():Node = build(Null, Nil)
			
			def build(children:Seq[Node]):Node = build(Null, children)
			
			def build(attributes:MetaData):Node = build(attributes, Nil)
			
			def build(attributes:MetaData, children:Seq[Node]):Node =
			{
				val scope = new NamespaceBinding(null, this.namespace, TopScope)
				return Elem(null, this.name, attributes, scope, children:_*)
			}
		}
		
		object ExtensionsManager
		{
			private val builders = mutable.ListBuffer[ExtensionBuilder[_]]()
			
			final def registerExtensionBuilder[T <: Extension](builder:ExtensionBuilder[T])
			{
				builders += builder
			}
			
			final def getExtension[T <: Extension](xml:Node):Option[T] =
			{
				try
				{
					if (0 == builders.length) return None
					
					val iterator = xml.child.iterator
					while (iterator.hasNext)
					{
						val node = iterator.next
						builders.find( builder => node.label == builder.name && node.scope.uri == builder.namespace ) match
						{
							case Some(builder) => return Some(builder.apply(node).asInstanceOf[T])
							case None => // continue
						}
					}
					
					return None
				}
				catch
				{
					case e:Exception => return None
				}
			}
			
			// well known extensions
			
			/* disco */
			registerExtensionBuilder(disco.InfoBuilder)
			registerExtensionBuilder(disco.ItemsBuilder)
			/* roster */
			registerExtensionBuilder(roster.Builder)
			/* forms */
			registerExtensionBuilder(forms.Builder)
			/* muc */
			registerExtensionBuilder(muc.Builder)
			registerExtensionBuilder(muc.user.Builder)
			registerExtensionBuilder(muc.owner.Builder)
			registerExtensionBuilder(muc.admin.Builder)
			/* pubsub */
			registerExtensionBuilder(pubsub.Builder)
			registerExtensionBuilder(pubsub.EventBuilder)
		}
		
		
	}
}