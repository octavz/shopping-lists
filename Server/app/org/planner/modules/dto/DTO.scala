package org.planner.modules.dto

import com.wordnik.swagger.annotations.{ApiModel, ApiModelProperty}
import org.planner.db._
import org.planner.modules._
import org.planner.util.{Constants, Gen, Time}
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.annotation.meta.field

case class LoginForm(email: String, password: String)

case class RegisterDTO(login: String, password: String) {

  def this(model: User) = this(model.login, model.password)

  def toModel = {
    val n = Time.now
    User(id = Gen.guid, login = login, providerToken = Some(login), created = n, updated = n, lastLogin = None, password = password, nick = login, userId = None, groupId = None)
  }
}

@ApiModel("UserDTO")
case class UserDTO(
                    @(ApiModelProperty@field)(required = true, hidden = false) login: String,
                    @(ApiModelProperty@field)(required = true, hidden = false) password: String,
                    @(ApiModelProperty@field)(required = false, hidden = true) id: String,
                    @(ApiModelProperty@field)(required = true, hidden = false) nick: String) {

  def this(model: User) = this(model.login, model.password, model.id, model.nick)

  def toModel = {
    val n = Time.now
    User(id = id, login = login, providerToken = Some(login), created = n, updated = n, lastLogin = None, password = password, nick = nick, userId = None, groupId = None)
  }

}

case class GroupDTO(id: Option[String], name: String, projectId: String) {
  def this(model: Group) = this(Some(model.id), model.name, model.projectId)

  def toModel(userId: String) = {
    val n = Time.now
    Group(id = id.getOrGuid, name = name, projectId = projectId, userId = userId, groupId = None, created = n, updated = n)
  }
}

@ApiModel("ProjectDTO")
case class ProjectDTO(@(ApiModelProperty@field)(required = false, hidden = true) id: Option[String],
                      @(ApiModelProperty@field)(required = true) name: String,
                      @(ApiModelProperty@field)(required = false) desc: Option[String],
                      @(ApiModelProperty@field)(required = false) parent: Option[String],
                      @(ApiModelProperty@field)(required = false) public: Boolean,
                      @(ApiModelProperty@field)(required = false, hidden = true, dataType = "int") perm: Option[Int],
                      @(ApiModelProperty@field)(required = false) groupId: Option[String],
                      @(ApiModelProperty@field)(required = false, hidden = true) userId: Option[String]) {

  def this(model: Project, group: Group) = this(
    id = Some(model.id),
    name = model.name,
    desc = model.description,
    parent = model.parentId,
    public = model.perm == 1,
    perm = Some(group.permProject),
    groupId = Some(group.id),
    userId = Some(model.userId)
  )

  def toModel() = {
    val n = Time.now
    Project(
      id = id.getOrGuid,
      userId = userId.getOrElse(throw new Exception("No user!")),
      name = name,
      description = desc,
      parentId = parent,
      created = n,
      updated = n,
      perm = if (public) 1 else 0)
  }
}

@ApiModel("TaskDTO")
case class TaskDTO(
                    @(ApiModelProperty@field)(required = false) id: Option[String],
                    @(ApiModelProperty@field)(required = true) subject: String,
                    @(ApiModelProperty@field)(required = false) desc: Option[String],
                    @(ApiModelProperty@field)(required = false) parent: Option[String],
                    @(ApiModelProperty@field)(required = false) projectId: Option[String],
                    @(ApiModelProperty@field)(required = false) public: Option[Boolean],
                    @(ApiModelProperty@field)(required = false, dataType = "int") perm: Option[Int],
                    @(ApiModelProperty@field)(required = false, hidden = true) userId: Option[String],
                    @(ApiModelProperty@field)(required = false) groupId: Option[String]) {

  def this(model: Task) = this(
    id = Some(model.id),
    subject = model.subject,
    desc = model.description,
    parent = model.parentId,
    projectId = Some(model.projectId),
    public = Some(model.perm == 1),
    perm = Some(model.perm),
    userId = Some(model.userId),
    groupId = Some(model.groupId))

  def toModel() = {
    val n = Time.now
    Task(
      id = id.getOrGuid,
      userId = userId.getOrElse(throw new Exception("User id not set for task.")),
      subject = subject,
      description = desc,
      projectId = projectId.getOrElse("Task has no project id"),
      parentId = parent,
      created = n,
      updated = n,
      perm = if (public.isDefined && public.get) 1 else 0,
      groupId = groupId.getOrElse(Constants.EMPTY_GROUP))
  }
}

case class ProjectListDTO(items: List[ProjectDTO], total: Int)

case class TaskListDTO(items: List[TaskDTO], total: Int)

case class StringDTO(value: String)

case class BooleanDTO(value: Boolean)

trait JsonDTOFormats extends BaseFormats with ConstraintReads {

  implicit val stringDTO = Json.format[StringDTO]

  implicit val booleanDTO = Json.format[BooleanDTO]

  implicit val userDTO = Json.format[UserDTO]

  implicit val taskDTO = Json.format[TaskDTO]

  implicit val tasksDTO = Json.format[TaskListDTO]

  implicit val registerDTO = (
    (__ \ 'login).format[String](maxLength[String](200) keepAnd email) ~
      (__ \ 'password).format[String](minLength[String](6) keepAnd maxLength[String](50))
    )(RegisterDTO, unlift(RegisterDTO.unapply))

  implicit val projectDtoRead = (
    (__ \ 'id).readNullable[String](maxLength[String](50)) ~
      (__ \ 'name).read[String](minLength[String](5) keepAnd maxLength[String](200)) ~
      (__ \ 'desc).readNullable[String](maxLength[String](1500)) ~
      (__ \ 'parent).readNullable[String](maxLength[String](50)) ~
      (__ \ 'public).read[Boolean] ~
      (__ \ 'perm).readNullable[Int](max(999)) ~
      (__ \ 'groupId).readNullable[String] ~
      (__ \ 'userId).readNullable[String]
    )(ProjectDTO)

  implicit val projectDtoWrite = Json.writes[ProjectDTO]

  implicit val projectListDto = Json.format[ProjectListDTO]

  implicit val groupDtoRead = (
    (__ \ 'id).readNullable[String](maxLength[String](50)) ~
      (__ \ 'name).read[String](minLength[String](5) keepAnd maxLength[String](200)) ~
      (__ \ 'projectId).read[String](maxLength[String](50))
    )(GroupDTO)

  implicit val groupDtoWrite = Json.writes[GroupDTO]

}
