package org.shopping.config

import javax.inject.Inject

import play.api.http.DefaultHttpFilters
import play.filters.cors.CORSFilter


class CORS @Inject()(corsFilter: CORSFilter)
  extends DefaultHttpFilters(corsFilter)

