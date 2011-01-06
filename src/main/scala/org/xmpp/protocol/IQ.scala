package org.xmpp
{
	package protocol
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol.Protocol._
		
		final object IQ
		{
			val TAG = "iq"
			
			def apply():IQ = apply(None, None, None, Some(IQTypeEnumeration.Get), None)
			
			def apply(kind:Option[IQTypeEnumeration.Value]):IQ = apply(None, None, None, kind, None)
			
			def apply(kind:Option[IQTypeEnumeration.Value], extensions:Option[Seq[Extension]]):IQ = apply(None, None, None, kind, extensions)
						
			def apply(id:Option[String], kind:Option[IQTypeEnumeration.Value], extensions:Option[Seq[Extension]]=None):IQ = apply(id, None, None, kind, extensions)
				
			def apply(id:Option[String], to:Option[JID], from:Option[JID], kind:Option[IQTypeEnumeration.Value], extensions:Option[Seq[Extension]]):IQ =
			{
				val xml = Stanza.build(TAG, id, to, from, kind, extensions)
				return new IQ(xml)
			}	
			
			def error(id:Option[String], to:Option[JID], from:Option[JID], condition:ErrorCondition.Value, description:Option[String]=None):IQ =
			{
				val xml = Stanza.error(TAG, id, to, from, condition, description)
				return new IQ(xml)
			}
		}
		
		class IQ(xml:Node) extends Stanza(xml)
		{
			val TypeEnumeration = IQTypeEnumeration
						
			final def result:IQ = IQ(this.id, this.to, this.from, Some(IQTypeEnumeration.Result), None)
			
			final def error(condition:ErrorCondition.Value, description:Option[String]=None):IQ = IQ.error(this.id, this.from, this.to, condition, description)
		}
				
		final object IQTypeEnumeration extends Enumeration
		{
			type value = Value
			
			val Unknown = Value("unknown") // internal use
			val Get = Value("get")
			val Set = Value("set")
			val Result = Value("result")
			val Error = Value("error")
		}
		
		object IQGet
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):IQGet = 
			{				
				val xml = Stanza.build(IQ.TAG, id, to, from, IQTypeEnumeration.Get.toString, extensions)
				return new IQGet(xml)	
			}
			
			def apply(xml:Node):IQGet = 
			{
				val get = new IQGet(xml)
				get.parse
				return get
			}
		}
		
		class IQGet(xml:Node) extends IQ(xml)
		{
			def result(extensions:Option[Seq[Extension]]):IQResult = IQResult(this.id, this.from, this.to, extensions)
		}
		
		object IQSet
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):IQSet = 
			{				
				val xml = Stanza.build(IQ.TAG, id, to, from, IQTypeEnumeration.Get.toString, extensions)
				return new IQSet(xml)	
			}
			
			def apply(xml:Node):IQSet = 
			{	
				val set = new IQSet(xml)
				set.parse
				return set
			}
		}
		
		class IQSet(xml:Node) extends IQ(xml)
		{
			def result(extensions:Option[Seq[Extension]]):IQResult = IQResult(this.id, this.from, this.to, extensions)
		}		
		
		object IQResult
		{
			def apply(id:Option[String], to:Option[JID], from:Option[JID], extensions:Option[Seq[Extension]]):IQResult = 
			{
				val xml = Stanza.build(IQ.TAG, id, to, from, IQTypeEnumeration.Result.toString, extensions)
				return new IQResult(xml)
			}
			
			def apply(xml:Node):IQResult = 
			{
				val result = new IQResult(xml)
				result.parse
				return result
			}
		}
			
		class IQResult(xml:Node) extends IQ(xml)
		{
			// getters
			private var _items:Option[Seq[IQItem]] = None
			private def items:Option[Seq[IQItem]] = _items
			
			override protected def parse
			{
				super.parse
			
				val itemsNodes = (this.xml \ "item")
				_items = if (0 == itemsNodes.length) None else Some(itemsNodes.map( node => IQItem(node) ))
			}			
		}
		
		object IQItem
		{
			val TAG = "item"
				
			def apply(attributes:Option[MetaData], children:Option[Seq[Node]]):IQItem = 
			{
				val kids = if (!children.isEmpty) children.get else null
				val metadata = if (!attributes.isEmpty) attributes.get else Null
				
				apply(Elem(null, TAG, metadata, TopScope, kids:_*))
			}
			
			def apply(xml:Node):IQItem = new IQItem(xml)
		}
		
		class IQItem(xml:Node) extends XmlWrapper(xml)
		{
			protected def parse
			{
			}
			
			parse
		}
	
		
		
	}
}
