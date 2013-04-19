/*
*** copied from net.liftweb.common
*/

package com.mishlabs.xmpp
package util

import org.slf4j.{Marker, Logger => SLF4JLogger, LoggerFactory}

private [xmpp] class Logger
{
    private lazy val logger:SLF4JLogger = LoggerFactory.getLogger(loggerNameFor(this.getClass))

    private def loggerNameFor(cls:Class[_]) =
    {
        val className = cls.getName
        if (className endsWith "$") className.substring(0, className.length - 1) else className
    }

    def assertLog(assertion: Boolean, msg: => String) = if (assertion) info(msg)

    /**
     * Log the value of v with trace and return v. Useful for tracing values in expressions
     */
    def trace[T](msg:String, v:T):T =
    {
        logger.trace(msg+": "+v.toString)
        v
    }

    def trace(msg: => AnyRef) = if (isTraceEnabled) logger.trace(String.valueOf(msg))
    def trace(msg: => AnyRef, t: Throwable) = if (isTraceEnabled) logger.trace(String.valueOf(msg), t)
    def trace(msg: => AnyRef, marker:  Marker) = if (isTraceEnabled) logger.trace(marker,String.valueOf(msg))
    def trace(msg: => AnyRef, t: Throwable, marker: => Marker) = if (isTraceEnabled) logger.trace(marker,String.valueOf(msg), t)
    def isTraceEnabled = logger.isTraceEnabled

    def debug(msg: => AnyRef) = if (isDebugEnabled) logger.debug(String.valueOf(msg))
    def debug(msg: => AnyRef, t:  Throwable) = if (isDebugEnabled) logger.debug(String.valueOf(msg), t)
    def debug(msg: => AnyRef, marker: Marker) = if (isDebugEnabled) logger.debug(marker, String.valueOf(msg))
    def debug(msg: => AnyRef, t: Throwable, marker: Marker) = if (isDebugEnabled) logger.debug(marker, String.valueOf(msg), t)
    def isDebugEnabled = logger.isDebugEnabled

    def info(msg: => AnyRef) = if (isInfoEnabled) logger.info(String.valueOf(msg))
    def info(msg: => AnyRef, t: => Throwable) = if (isInfoEnabled) logger.info(String.valueOf(msg), t)
    def info(msg: => AnyRef, marker: Marker) = if (isInfoEnabled) logger.info(marker,String.valueOf(msg))
    def info(msg: => AnyRef, t: Throwable, marker: Marker) = if (isInfoEnabled) logger.info(marker,String.valueOf(msg), t)
    def isInfoEnabled = logger.isInfoEnabled

    def warn(msg: => AnyRef) = if (isWarnEnabled) logger.warn(String.valueOf(msg))
    def warn(msg: => AnyRef, t: Throwable) = if (isWarnEnabled) logger.warn(String.valueOf(msg), t)
    def warn(msg: => AnyRef, marker: Marker) = if (isWarnEnabled) logger.warn(marker,String.valueOf(msg))
    def warn(msg: => AnyRef, t: Throwable, marker: Marker) = if (isWarnEnabled) logger.warn(marker,String.valueOf(msg), t)
    def isWarnEnabled = logger.isWarnEnabled

    def error(msg: => AnyRef) = if (isErrorEnabled) logger.error(String.valueOf(msg))
    def error(msg: => AnyRef, t: Throwable) = if (isErrorEnabled) logger.error(String.valueOf(msg), t)
    def error(msg: => AnyRef, marker: Marker) = if (isErrorEnabled) logger.error(marker,String.valueOf(msg))
    def error(msg: => AnyRef, t: Throwable, marker: Marker) = if (isErrorEnabled) logger.error(marker,String.valueOf(msg), t)
    def isErrorEnabled = logger.isErrorEnabled
}