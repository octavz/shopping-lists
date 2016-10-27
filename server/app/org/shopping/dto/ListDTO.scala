package org.shopping.dto

import org.shopping.db._
import org.shopping.modules._
import org.shopping.util.Time

case class ListDTO(id: Option[String], name: String, description: Option[String], userId: Option[String], created: Long) {

  def this(model: FullList) = this(
    id = Some(model.inst.id),
    name = model.listDef.name,
    description = model.listDef.description,
    userId = Some(model.listDef.userId),
    created = model.inst.createdClient
  )

  def this(model: ListDef) = this(
    id = Some(model.id),
    name = model.name,
    description = model.description,
    userId = Some(model.userId),
    created = model.created
  )

  def toModel: ListDef = {
    val n = Time.now()

    ListDef(
      id = id.getOrGuid,
      userId = userId.getOrElse(throw new Exception("No user attached to this dto!")),
      name = name,
      description = description,
      created = n,
      updated = n
    )
  }
}
