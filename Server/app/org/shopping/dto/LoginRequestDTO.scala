package org.shopping.dto

/**
  * Created by octav on 11.09.2016.
  */
//new ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "form", defaultValue = "aaa@aaa.com")
//, new ApiImplicitParam(name = "password", required = true, dataType = "String", paramType = "form", defaultValue = "123456")
//, new ApiImplicitParam(name = "client_id", required = true, dataType = "String", paramType = "form", defaultValue = "1")
//, new ApiImplicitParam(name = "grant_type", required = true, dataType = "String", paramType = "form", defaultValue = "password")
//, new ApiImplicitParam(name = "client_secret", required = true, dataType = "String", paramType = "form", defaultValue = "secret")

case class LoginRequestDTO(userName: String, password: String, clientId: String, grantType: String, clientSecret: String)
