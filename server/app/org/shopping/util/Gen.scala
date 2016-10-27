package org.shopping.util

import java.util.Date

object Constants {
  val EMPTY_GROUP = ""
  val DEFAULT_GROUP_TYPE = 1
  val STATUS_DELETE: Short = 5
}


object Gen {

  /**
    * generates a GUID
    *
    * @return new GUID
    */
  def guid = java.util.UUID.randomUUID().toString

  /**
    * generates an option GUID
    *
    * @return Option GUID
    */
  def guido = Some(guid)
}

object Time {
  def now() = System.currentTimeMillis() / 1000L

  def dateToTs(date: java.util.Date) = date.getTime / 1000L

  def tsToDate(ts: Long) = new Date(ts * 1000)

  def nowo = Some(now)

}
