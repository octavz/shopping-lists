package org.shopping.dal

import org.shopping.db._

trait ListDAL {

  def insertList(model: ListDef): DAL[ListDef]

  def addListItems(model: Seq[ListItem]): DAL[Seq[ListItem]]

  def getUserLists(uid: String, offset: Int, count: Int): DAL[(Seq[ListDef], Int)]

  def updateLists(list: ListDef): DAL[ListDef]

  def getListById(id: String): DAL[Option[ListDef]]

  def addListItem(model: ListItem): DAL[ListItem]

  def getListItemsByList(listId: String): DAL[Seq[ListItem]]

  def getListUsers(listId: String):DAL[Seq[String]]

}
