package org.xmpp
{
	package util
	{
		import scala.collection._
		import scala.actors._
		
		case class Start
		case class Stop
		
		abstract class ScheduledJob(interval:Int) extends Actor
		{
			start
			
			def act
			{
				loop
				{
					reactWithin(interval)
					{
						case Start => doJob
						case Stop => exit
						case TIMEOUT => doJob
					}
				}
			}
			
			def doJob
		}
		
		object ScheduledJobsManager
		{
			private val jobs:mutable.ListMap[String, ScheduledJob] = new mutable.ListMap[String, ScheduledJob]()
			
			def registerJob(name:String, job:ScheduledJob)
			{
				require(null != name)
				require(null != job)
				
				if (jobs.contains(name)) throw new Exception("a job with this name (" + name + ") already exists")
				jobs += name -> job
			}
			
			def unregisterJob(name:String)
			{
				if (!jobs.contains(name)) throw new Exception("a job with this name (" + name + ") does not exist")
				jobs - name
			}
			
			def get(name:String):Option[ScheduledJob] = jobs.get(name)
			
			def startJob(name:String)
			{
				jobs.get(name) match
				{
					case Some(job) => job ! Start
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
				jobs.values.foreach({ job => job ! Start })
			}
			
			def stopAll
			{
				jobs.values.foreach({ job => job ! Stop })
			}			
		}
	}
}