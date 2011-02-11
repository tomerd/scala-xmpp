package org.simbit.xmpp
{
	package component
	{
		import scala.collection._
				
		object XmppComponentsManager 
		{
			private var components:mutable.ListMap[String, XmppComponent] = null
			
			def registerComponent(subdomain:String, component:XmppComponent, host:String, port:Int, secret:String)
			{
				if (null == components) components = new mutable.ListMap[String, XmppComponent]()
				if (components.contains(subdomain)) throw new IllegalArgumentException(subdomain + " is already in use by another component")
				components += subdomain -> component
				component.start(subdomain, host, port, secret)
			}
			
			def unregisterComponent(subdomain:String)
			{
				if (!components.contains(subdomain)) throw new IllegalArgumentException("unknown subdomain " + subdomain)
				components.get(subdomain).get.shutdown
				components - subdomain
			}
					
			def shutdown()
			{
				components.values.foreach( component => component.shutdown )
				components.clear();
			}
					
		}
		
	}
}