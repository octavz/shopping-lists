package org.shopping.modules.core

import org.shopping.modules.Result
import org.shopping.dto._

  trait ListModule extends BaseModule{

    def insertList(project: ListDTO): Result[ListDTO]

    def updateList(project: ListDTO): Result[ListDTO]

    def deleteList(projectId: String): Result[BooleanDTO]

    def getUserLists(id: String, offset: Int, count: Int): Result[ProjectListDTO]

    def insertListItem(task: ListItemDTO): Result[ListItemDTO]

    def getListItems(projectId: String, offset: Int, count: Int): Result[ListItemListDTO]


}
