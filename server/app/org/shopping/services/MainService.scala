package org.shopping.services

import org.shopping.dto._

trait MainService extends BaseService {

  def sync(data: SyncDTO): Result[SyncDTO]

}
