package org.shopping.dal

import org.shopping.db._
import org.shopping.models.{ListDef, ListDefProduct}

trait ListRepo {

  def insertList(model: ListDef): Repo[ListDef]

  def getUserLists(uid: String, offset: Int, count: Int): Repo[(Seq[ListDef], Int)]

  def updateList(list: ListDef): Repo[ListDef]

  def getListDefById(id: String): Repo[Option[ListDef]]

  def replaceListItems(listId: String, model: Seq[ListDefProduct]): Repo[Seq[ListDefProduct]]

  def getListProductsByList(listDefId: String): Repo[Seq[ListDefProduct]]

  def getListUsers(listId: String): Repo[Seq[String]]

  def updateListProduct(listProduct: ListDefProduct): Repo[ListDefProduct]

  def updateBatchedBought(listId: String, ids: Map[String, Boolean]): Repo[Int]
}
