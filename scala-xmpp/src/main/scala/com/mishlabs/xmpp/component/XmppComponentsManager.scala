package com.mishlabs.xmpp
package component

import scala.collection._

object XmppComponentsManager
{
    private var components:mutable.ListMap[String, XmppComponent] = null

    def register(component:XmppComponent, subdomain:String, host:String, port:Int, secret:String)
    {
        if (null == components) components = new mutable.ListMap[String, XmppComponent]()
        if (components.contains(subdomain)) throw new IllegalArgumentException(subdomain + " is already in use by another component")
        components += subdomain -> component
        component.startup(subdomain, host, port, secret)
    }

    def unregister(subdomain:String)
    {
        if (!components.contains(subdomain)) throw new IllegalArgumentException("unknown subdomain " + subdomain)
        components.get(subdomain).get.shutdown()
        components - subdomain
    }

    def shutdown()
    {
        components.values.foreach( _.shutdown() )
        components.clear()
    }
}