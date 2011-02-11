package org.simbit.xmpp
{
	package protocol
	{
		object JID
		{
			def apply(string:String):JID =
			{
				val array1 = string.split("@")
				val array2 = if (2 == array1.length) array1(1).split("/") else string.split("/")
				
				val node = if (2 == array1.length) array1(0) else ""				
				val resource = if (2 == array2.length) array2(1) else ""
				val domain = if (2 == array2.length) array2(0) else if (2 == array1.length) array1(1) else string
				
				return JID(node, domain, resource)
			}
		}
		
		case class JID(node:String, domain:String, resource:String)
		{						
			private var _display:Option[String] = None
			
			def validate
			{
				// TODO: implement this accoring to the XMPP spec
			}
			
			override def toString:String =
			{
				_display match 
				{
					case Some(display) => display
					case None =>
					{
						val builder = new StringBuilder()
						if (null != node && !node.isEmpty) builder.append(this.node).append("@")
						builder.append(this.domain)
						if (null != resource && !resource.isEmpty) builder.append("/").append(this.resource)
						_display = Some(builder.toString)
						return _display.get
					}
				}
			}
			
			override def equals(other:Any) = other match 
			{
				case jid:JID => jid.toString == this.toString 
				case _ => false
			}
		}
	}
}