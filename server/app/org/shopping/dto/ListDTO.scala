package org.shopping.dto

import org.shopping.db._
import org.shopping.modules._
import org.shopping.util.Time

case class ListDTO(id: Option[String], name: String, description: Option[String], userId: Option[String]) {

  def this(model: List) = this(
    id = Some(model.id),
    name = model.name,
    description = model.description,
    userId = Some(model.userId)
  )

  def toModel = {
    val n = Time.now
    List(
      id = id.getOrGuid,
      userId = userId.getOrElse(throw new Exception("No user attached to this dto!")),
      name = name,
      description = description,
      created = n,
      updated = n)
  }
}
