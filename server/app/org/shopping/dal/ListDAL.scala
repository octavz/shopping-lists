package org.shopping.dal

import org.shopping.db._

trait ListDAL {

  def insertList(model: FullList): DAL[FullList]

  def getUserLists(uid: String, offset: Int, count: Int): DAL[(Seq[FullList], Int)]

  def updateList(list: ListDef): DAL[ListDef]

  def getListById(id: String): DAL[Option[ListInst]]

  def getListDefById(id: String): DAL[Option[ListDef]]

  def addListProduct(model: ListProduct): DAL[ListProduct]

  def addListProducts(model: Seq[ListProduct]): DAL[Seq[ListProduct]]

  def getListProductsByListId(listId: String): DAL[Seq[ListProduct]]

  def addListDefProducts(model: Seq[ListDefProduct]): DAL[Seq[ListDefProduct]]

  def getListDefProductsByListId(listDefId: String): DAL[Seq[ListDefProduct]]

  def getListUsers(listId: String):DAL[Seq[String]]

}
