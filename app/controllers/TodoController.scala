package controllers

import javax.inject._
import play.api.mvc._
import services._

import play.api.data._
import play.api.data.Forms._

class TodoController @Inject()(todoService: TodoService, mcc: MessagesControllerComponents)
extends MessagesAbstractController(mcc){
  def helloworld() = Action {
    implicit  request: MessagesRequest[AnyContent] =>
    Ok("hello")
  }
  def list() = Action { implicit request: MessagesRequest[AnyContent] =>
    // val message: String = "koko"
    // val items: Seq[Todo] = Seq(Todo("Todo1"), Todo("Todo2"))
    val items: Seq[Todo] = todoService.list()
    Ok(views.html.list(items))
  }
  val todoForm: Form[String] = Form("name" ->nonEmptyText)
  def todoNew = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.createForm(todoForm))
  }
  def todoAdd() = Action { implicit request: MessagesRequest[AnyContent] =>
    val name: String = todoForm.bindFromRequest().get
    println(name)
    todoService.insert(Todo(id = None,name))
    // Ok("Save")
    Redirect(routes.TodoController.list())
  }
  def todoEdit(todoId: Long) = Action { implicit request: MessagesRequest[AnyContent] =>
    todoService.findById(todoId).map { todo =>
      Ok(views.html.editForm(todoId, todoForm.fill(todo.name)))
    }.getOrElse(NotFound)
  }
  def todoUpdate(todoId: Long) = Action {implicit request: MessagesRequest[AnyContent] =>
    val name: String = todoForm.bindFromRequest().get
    todoService.update(todoId, Todo(Some(todoId),name))
    Redirect(routes.TodoController.list())
  }
  def todoDelete(todoId: Long) = Action { implicit request: MessagesRequest[AnyContent] =>
    todoService.delete(todoId)
    Redirect(routes.TodoController.list())
  }
}
