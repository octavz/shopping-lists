package org.shopping.dto

import org.shopping.db._
import org.shopping.modules._
import org.shopping.util.Time

case class ListDTO(id: Option[String], name: String, desc: Option[String], userId: Option[String]) {

  def this(model: List) = this(
    id = Some(model.id),
    name = model.name,
    desc = model.description,
    userId = Some(model.userId)
  )

  def toModel() = {
    val n = Time.now
    List(
      id = id.getOrGuid,
      userId = userId.getOrElse(throw new Exception("No user!")),
      name = name,
      description = desc,
      created = n,
      updated = n)
  }
}
