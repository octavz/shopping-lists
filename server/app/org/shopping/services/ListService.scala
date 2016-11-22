package org.shopping.services

import org.shopping.dto._

trait ListService extends BaseService {

  def insertList(list: ListDTO)(implicit authData: AuthData): Result[ListDTO]

  def updateList(list: ListDTO)(implicit authData: AuthData): Result[ListDTO]

  def updateLists(lists: ListsDTO)(implicit authData: AuthData): Result[ListsDTO]

  def insertLists(lists: ListsDTO)(implicit authData: AuthData): Result[ListsDTO]

  def deleteList(list: String)(implicit authData: AuthData): Result[BooleanDTO]

  def getUserLists(userId: String, offset: Int, count: Int)(implicit authData: AuthData): Result[ListsDTO]

  def addListItems(listItems: ListItemsDTO)(implicit authData: AuthData): Result[ListItemsDTO]

  def getListItems(listId: String)(implicit authData: AuthData): Result[ListItemsDTO]

}
