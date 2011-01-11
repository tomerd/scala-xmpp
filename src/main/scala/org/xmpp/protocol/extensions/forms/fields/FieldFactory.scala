package org.xmpp
{
	package protocol.extensions.forms.fields
	{
		import scala.collection._
		import scala.xml._
		
		import org.xmpp.protocol._
		
		import org.xmpp.protocol.Protocol._
			
		protected[forms] object FieldFactory
		{
			def create(xml:Node):Field =
			{
				xml match
				{
					case field @ <field/> if !(field \ "value").isEmpty => SimpleField(xml)
					case field @ <field/> if !(field \ "option").isEmpty => OptionsField(xml)
					case _ => throw new Exception("unknown field")
				}
			}
		}
	
	}
}
