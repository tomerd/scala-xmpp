package org.simbit.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.simbit.util._
		
		import org.simbit.xmpp.protocol._
		import org.simbit.xmpp.protocol.extensions._
		
		import org.simbit.xmpp.protocol.Protocol._
		
		trait ExtensionBuilder[T <: Extension]
		{
			val tag:String
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
				return Elem(null, this.tag, attributes, scope, children:_*)
			}
		}
		
		object ExtensionsManager extends Logger
		{
			private val builders = mutable.HashMap[String, ExtensionBuilder[_]]()
			
			final def registerBuilder[T <: Extension](builder:ExtensionBuilder[T])
			{
				val key = getKey(builder)
				if (builders.contains(key)) 
				{
					warning("an extension builder for this tag and namespace already exists, ignoring")
					return
				}
				builders += key -> builder
			}
			
			final def getExtensions[T <: Extension](xml:Node):Option[Seq[T]] =
			{
				try
				{
					if (0 == builders.size) return None
					val buffer = mutable.ListBuffer[T]()
					
					val iterator = xml.child.iterator
					while (iterator.hasNext)
					{
						val node = iterator.next
						builders.get(getKey(node)) match
						{
							case Some(builder) => 
							{
								builder.apply(node) match
								{
									case extension:T => buffer += extension
									case _ => warning(builder + " returned invalid extension type for " + node.label + " " + node.scope.uri)
								}
							}
							case None => // continue
						}
					}
					
					return if (0 == buffer.length) None else Some(buffer)
				}
				catch
				{
					case e:Exception => return None
				}
			}
			
			private def getKey(builder:ExtensionBuilder[_]):String = getKey(builder.tag, builder.namespace)
			private def getKey(node:Node):String = getKey(node.label, node.scope.uri)
			private def getKey(tag:String, namespace:String):String = tag + "~" + namespace
			
			// well known extensions
			
			/* disco */
			registerBuilder(disco.InfoBuilder)
			registerBuilder(disco.ItemsBuilder)
			/* roster */
			registerBuilder(roster.Builder)
			/* forms */
			registerBuilder(forms.Builder)
			/* muc */
			registerBuilder(muc.general.Builder)
			registerBuilder(muc.user.Builder)
			registerBuilder(muc.owner.Builder)
			registerBuilder(muc.admin.Builder)
			/* pubsub */
			registerBuilder(pubsub.Builder)
			registerBuilder(pubsub.EventBuilder)
		}		
		
	}
}