package com.mishlabs.xmpp
{
	package util
	{
		import scala.collection._
		import scala.actors._
		
		case class Act
		case class Stop
		
		private class ScheduledJob(job:() => Unit, interval:Int) extends Actor
		{
			def act
			{
				loop
				{
					reactWithin(interval)
					{
						case Act => job()
						case Stop => exit
						case TIMEOUT => this ! Act
					}
				}
			}
		}
		
		object ScheduledJobsManager
		{
			private val jobs:mutable.ListMap[String, ScheduledJob] = new mutable.ListMap[String, ScheduledJob]()
			
			def registerJob(name:String, job:() => Unit, interval:Int)
			{
				require(null != name)
				require(null != job)
				
				if (jobs.contains(name)) throw new Exception("a job with this name (" + name + ") already exists")
				jobs += name -> new ScheduledJob(job, interval)
			}
			
			def unregisterJob(name:String)
			{
				if (!jobs.contains(name)) throw new Exception("a job with this name (" + name + ") does not exist")
				jobs - name
			}
			
			def startJob(name:String)
			{
				jobs.get(name) match
				{
					case Some(job) => job.start
					case _ => throw new Exception("a job with this name (" + name + ") does not exist")
				}
			}
			
			def stopJob(name:String)
			{
				jobs.get(name) match
				{
					case Some(job) => job ! Stop
					case _ => throw new Exception("a job with this name (" + name + ") does not exist")
				}
			}			
			
			def startAll
			{
				jobs.values.foreach({ job => job.start })
			}
			
			def stopAll
			{
				jobs.values.foreach({ job => job ! Stop })
			}
			
			private def get(name:String):Option[ScheduledJob] = jobs.get(name)
		}
	}
}