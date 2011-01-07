package org.xmpp
{
	package protocol
	{
		object JID
		{
			def apply(string:String):JID =
			{
				val array1 = string.split("@")
				if (array1.length < 2) return JID(null, string, null)
				var array2 = array1(1).split("/")
				if (array2.length < 2) return JID(array1(0), array1(1), null)
				return JID(array1(0), array2(0), array2(1)) 				
			}
		}
		
		case class JID(node:String, domain:String, resource:String)
		{
			private var _display:Option[String] = None
			
			def validate
			{
				// TODO: implement this accorind to XMPP spec
			}
			
			override def toString:String =
			{
				_display match 
				{
					case Some(display) => display
					case None =>
					{
						val builder = new StringBuilder()
						if (null != node) builder.append(this.node).append("@")
						builder.append(this.domain)
						if (null != resource) builder.append("/").append(this.resource)
						_display = Some(builder.toString)
						return _display.get
					}
				}
			}
		}
	}
}