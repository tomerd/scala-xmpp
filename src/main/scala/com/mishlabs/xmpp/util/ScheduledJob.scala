package com.mishlabs.xmpp
{
	package util
	{
  import scala.collection._
  import akka.actor._
  import scala.concurrent.duration._
  import com.mishlabs.xmpp.util.Stop
  import com.mishlabs.xmpp.util.Act
  import scala.Some

    case class Act()
    case class Stop()
    case class Run()

		private class ScheduledJob(job:() => Unit, interval:Int) extends Actor
		{
      var running = false

      context.setReceiveTimeout(interval millisecond)
      def receive = {
        case Act => job()
        case Run => running = true
        case Stop => context.stop(self)
        case ReceiveTimeout => if (running) self ! Act
      }
		}

		object ScheduledJobsManager
		{
			private val jobs:mutable.ListMap[String, ActorRef] = new mutable.ListMap[String, ActorRef]()
      private val system = ActorSystem("default")

			def registerJob(name:String, job:() => Unit, interval:Int)
			{
				require(null != name)
				require(null != job)

				if (jobs.contains(name)) throw new Exception("a job with this name (" + name + ") already exists")
				jobs += name -> system.actorOf(Props(new ScheduledJob(job, interval)))
			}

			def unregisterJob(name:String)
			{
				if (!jobs.contains(name)) throw new Exception("a job with this name (" + name + ") does not exist")
        jobs.get(name).foreach(_ ! Stop)
				jobs - name
			}

			def startJob(name:String)
			{
				jobs.get(name) match
				{
					case Some(job) => job ! Run
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
				jobs.values.foreach({ job => job ! Run })
			}

			def stopAll
			{
				jobs.values.foreach({ job => job ! Stop })
			}

			private def get(name:String):Option[ActorRef] = jobs.get(name)
		}
	}
}