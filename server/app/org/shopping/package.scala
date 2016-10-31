package org

import org.shopping.dto.ErrorDTO
import org.shopping.services.Result
import play.api.libs.json.{Json, Writes}
import play.api.mvc.Results

import scala.concurrent.ExecutionContext.Implicits.global

package object shopping {

  //  object Json {
  //    val mapper = new ObjectMapper()
  //    mapper.registerModule(DefaultScalaModule)
  //
  //    def apply(): ObjectMapper = mapper
  //  }
  //
  //  implicit class JsonSerializer[T <: AnyRef](val obj: T)(implicit m: Manifest[T]) {
  //    def json = {
  //      obj match {//override Either serialization
  //        case o: Either[_, _] =>
  //          o match {
  //            case Left(l) => Json().writeValueAsString(l)
  //            case Right(r) => Json().writeValueAsString(r)
  //          }
  //        case _ =>
  //          Json().writeValueAsString(obj)
  //      }
  //    }
  //
  //  }
  //
  //  implicit class JsonStrinDeserializer[T <: AnyRef](val obj: String) {
  //    def fromJson[T](implicit m: Manifest[T]): T = Json().readValue(obj, m.runtimeClass).asInstanceOf[T]
  //  }
  //
  //  implicit class JsonJsValDeserializer[T <: AnyRef](val obj: JsValue) {
  //    def fromJson[T](implicit m: Manifest[T]): T = Json().readValue(obj.toString(), m.runtimeClass).asInstanceOf[T]
  //  }
  //
  implicit class ResultTransformer[T <: AnyRef](val result: Result[T]) extends Results {
    def toResponse(implicit m: Writes[T]) = result map {
      case Right(r) => Ok(Json.toJson(r).toString())
      case Left(ErrorDTO(code, msg)) => BadRequest(Json.obj("errCode" -> code, "errMessage" -> msg))

    }
  }


}
