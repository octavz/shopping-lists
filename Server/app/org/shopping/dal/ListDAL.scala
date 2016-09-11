package org.shopping.dal

import org.shopping.db._

trait ListDAL {

  def insertList(model: List): DAL[List]

  def getUserLists(uid: String, offset: Int, count: Int): DAL[(Seq[List], Int)]

  def updateLists(list: List): DAL[List]

  def getListById(id: String): DAL[Option[List]]

  def addListItem(model: ListItem): DAL[ListItem]

  def getListItemsByListAndUser(listId: String, userId: String, offset: Int, count: Int): DAL[(Seq[ListItem], Int)]


}
