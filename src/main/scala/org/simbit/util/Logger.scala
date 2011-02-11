package org.simbit.util

trait Logger 
{
	// FIXME: implement this, use some logging facet like SLF4J or configgy
	def debug(message:String)
	{
		println("DEBUG: " + message)
	}
	
	def info(message:String)
	{
		println("INFO: " + message)
	}
	
	def warning(message:String)
	{
		println("WARNING: " + message)
	}
	
	def error(exception:Exception) 
	{
		error(exception.toString)
	}
	
	def error(message:String)
	{
		println("ERROR: " + message)
	}
}