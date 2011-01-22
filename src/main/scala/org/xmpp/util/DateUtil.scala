package org.xmpp.util
{
	import java.text.DateFormat
	import java.text.SimpleDateFormat
	import java.text.ParseException
	import java.util.Date
	
	object DateUtil 
	{
		val xmppFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
		
		def parse(text:String):Date = xmppFormat.parse(text)
		
		def format(date:Date):String = xmppFormat.format(date)
	}

}