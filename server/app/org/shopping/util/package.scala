package org.shopping

package object util {
  def currentMethod(): String = Thread.currentThread.getStackTrace()(2).getMethodName

}
