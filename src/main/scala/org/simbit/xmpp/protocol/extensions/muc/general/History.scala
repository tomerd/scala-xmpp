package org.simbit.xmpp
{
	package protocol.extensions.muc	
	{
		package general
		{
			import java.util.Date
			
			import scala.collection._
			import scala.xml._
			
			import org.simbit.xmpp.util._
			
			import org.simbit.xmpp.protocol._
			import org.simbit.xmpp.protocol.presence._
			import org.simbit.xmpp.protocol.extensions._
			import org.simbit.xmpp.protocol.extensions.muc._
			
			import org.simbit.xmpp.protocol.Protocol._
					
			object History 
			{
				val tag = "history"
				
				def apply(since:Option[Date], seconds:Option[Int], maxstanzas:Option[Int], maxchars:Option[Int]):History = 
				{
					var metadata:MetaData = Null
					if (!since.isEmpty) metadata = metadata.append(new UnprefixedAttribute("since", Text(DateUtil.format(since.get)), Null))
					if (!seconds.isEmpty) metadata = metadata.append(new UnprefixedAttribute("seconds", Text(seconds.get.toString), Null))
					if (!maxstanzas.isEmpty) metadata = metadata.append(new UnprefixedAttribute("maxstanzas", Text(maxstanzas.get.toString), Null))
					if (!maxchars.isEmpty) metadata = metadata.append(new UnprefixedAttribute("maxchars", Text(maxchars.get.toString), Null))
					
					apply(Builder.build(Elem(null, tag, metadata, TopScope)))
				}
				
				def apply(xml:Node):History = History(xml)
			}
		
			class History(xml:Node) extends X(xml)
			{
				private val historyNode = (xml \ History.tag)(0)
				
				val since:Option[Date] = 
				{
					val since = (this.historyNode \ "@since").text
					if (since.isEmpty) None else Some(DateUtil.parse(since))
				}
				
				val seconds:Option[Int] =
				{
					val seconds = (this.historyNode \ "@seconds").text
					if (seconds.isEmpty) None else Some(seconds.toInt)
				}
				
				val maxstanzas:Option[Int] =
				{
					val maxstanzas = (this.historyNode \ "@maxstanzas").text
					if (maxstanzas.isEmpty) None else Some(maxstanzas.toInt)
				}
				
				val maxchars:Option[Int] = 
				{
					val maxchars = (this.historyNode \ "@maxchars").text
					if (maxchars.isEmpty) None else Some(maxchars.toInt)
				}
				
			}
			
		} 
	}	
}