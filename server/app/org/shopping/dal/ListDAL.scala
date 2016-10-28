package org.shopping.dal

import org.shopping.db._

trait ListDAL {

  def insertList(model: ListDef): DAL[ListDef]

  def getUserLists(uid: String, offset: Int, count: Int): DAL[(Seq[ListDef], Int)]

  def updateList(list: ListDef): DAL[ListDef]

  def getListDefById(id: String): DAL[Option[ListDef]]

  def addListDefProducts(listId: String, model: Seq[ListDefProduct]): DAL[Seq[ListDefProduct]]

  def getListProductsByList(listDefId: String): DAL[Seq[ListDefProduct]]

  def getListUsers(listId: String): DAL[Seq[String]]

  def updateListProduct(listProduct: ListDefProduct): DAL[ListDefProduct]

  def updateBatchedBought(listId: String, ids: Map[String, Boolean]): DAL[Int]
}
