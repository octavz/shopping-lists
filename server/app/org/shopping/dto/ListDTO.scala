package org.shopping.dto

import java.sql.Timestamp

import org.shopping.db._
import org.shopping.modules._
import org.shopping.util.Time

case class ListDTO(id: Option[String], name: String, description: Option[String], userId: Option[String], created: Long) {

  def this(model: List) = this(
    id = Some(model.id),
    name = model.name,
    description = model.description,
    userId = Some(model.userId),
    created = model.createdClient.getTime / 1000
  )

  def toModel = {
    val n = Time.now
    List(
      id = id.getOrGuid,
      userId = userId.getOrElse(throw new Exception("No user attached to this dto!")),
      name = name,
      description = description,
      created = n,
      updated = n,
      createdClient = new Timestamp(created * 1000)
    )
  }
}
