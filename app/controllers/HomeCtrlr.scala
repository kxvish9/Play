package controllers
import Model.EmployeeVO
import play.api.libs.json.Format.GenericFormat
import javax.inject._
import play.api.libs.json.{JsValue, Json}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import services.EmployeeService
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,val employeeService: EmployeeService) extends BaseController {
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  def getEmployee(company:String): Action[AnyContent] = Action async { implicit request: Request[AnyContent] =>
    employeeService.getEmployee(company).map {
      result => if(result.isEmpty)
        {
          NotFound("DATA_NOT_FOUND")
        }
      else {
          Ok(Json.toJson(result))
        }
    }
  }
  def getEmployeeById(company:String,id :Int): Action[AnyContent] = Action async { implicit request: Request[AnyContent] =>
    employeeService.getEmployeeById(company,id).map {
      result => if(result.isEmpty)
        {
          NotFound("DATA_NOT_FOUND")
        }
      else
        {
          Ok(Json.toJson(result))
        }
    }
  }
  def addEmployee(): Action[JsValue] = Action(parse.json) async { implicit request =>

    request.body.validate[EmployeeVO].map{

      employee => employeeService.addEmployee(employee).map{
        result => Ok(Json.toJson(result))
      }
    }.recoverTotal(
      e => Future{NotAcceptable("BAD_REQUEST")}
      )

  }
  def deleteEmployee(id:Int): Action[AnyContent] = Action async { implicit request:Request[AnyContent] =>

    employeeService.deleteEmployee(id).map{
      case 1=> Ok("DELETED")
      case _=> NotFound("DATA_NOT_FOUND")
    }

  }
  def updateEmployee(company:String,id:Int): Action[JsValue] = Action(parse.json) async { implicit request =>

    val employee = request.body.as(EmployeeVO.reads)
    val resultObj = employeeService.update(company,id,employee)
    val result = Await.result(resultObj,Duration.Inf)
    if(result!=null)
      {
        Future{Ok(Json.toJson(result))}
      }
      else {
      Future{NotFound("DATA_NOT_FOUND")}
    }
  }
}
