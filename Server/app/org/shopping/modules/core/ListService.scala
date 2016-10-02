package org.shopping.modules.core

import org.shopping.modules.Result
import org.shopping.dto._

trait ListService extends BaseService {

  def insertList(list: ListDTO): Result[ListDTO]

  def updateList(list: ListDTO): Result[ListDTO]

  def deleteList(list: String): Result[BooleanDTO]

  def getUserLists(userId: String, offset: Int, count: Int): Result[ListsDTO]

  def addListItems(listId: String, listItems: ListItemsDTO): Result[ListItemsDTO]

  def getListItems(listId: String): Result[ListItemsDTO]

}
