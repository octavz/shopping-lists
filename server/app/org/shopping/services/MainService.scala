package org.shopping.services

import org.shopping.dto._

trait MainService extends BaseService {

  def sync(data: SyncDTO)(implicit authData: AuthData): Result[SyncDTO]

}
