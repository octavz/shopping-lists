package org.shopping.dto

import org.shopping.db._
import org.shopping.modules._
import org.shopping.util.Time

case class ListDTO(id: Option[String], name: String, description: Option[String], userId: Option[String], created: Long) {

  def this(model: ListDef) = this(
    id = Some(model.id),
    name = model.name,
    description = model.description,
    userId = Some(model.userId),
    created = model.createdClient
  )

  def toModel: ListDef = {
    val n = Time.now()

    ListDef(
      id = id.getOrGuid,
      userId = userId.getOrElse(throw new Exception("No user attached to this dto!")),
      name = name,
      description = description,
      createdClient = created,
      created = n,
      updated = n
    )
  }
}
