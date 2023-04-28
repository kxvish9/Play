package Model
import slick.jdbc.MySQLProfile.api._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsObject, JsPath, Json, Reads, Writes}
case class EmployeeVO(employeeId: Option[Int],company: String,firstName: String,lastName: String,email: String,mobile: String,salary: Int)
class EmployeeTableDef(tag: Tag) extends Table[EmployeeVO](tag,"Employee"){
  def employeeId = column[Option[Int]]("employeeID",O.PrimaryKey,O.AutoInc)
  def company = column[String]("Company")
  def lastName = column[String]("LastName")
  def firstName = column[String]("FirstName")
  def email = column[String]("Email")
  def mobile = column[String]("ContactNumber")
  def salary = column[Int]("Salary")
  override def * =
    (employeeId,company,firstName,lastName,email,mobile,salary) <>  ( (EmployeeVO.apply _).tupled,EmployeeVO.unapply )
}
object EmployeeVO{
  implicit val writes: Writes[EmployeeVO] = new Writes[EmployeeVO] {
    override def writes(o: EmployeeVO): JsObject = Json.obj(
      "EmployeeId" -> o.employeeId,
      "Company" -> o.company,
      "LastName" -> o.lastName,
      "FirstName" -> o.firstName,
      "Email" -> o.email,
      "ContactNumber"  -> o.mobile,
      "Salary" -> o.salary
    )
  }
  implicit val reads: Reads[EmployeeVO] = (
    (JsPath \ "EmployeeId").readNullable[Int] and
      (JsPath \ "Company").read[String] and
      (JsPath \ "LastName").read[String] and
      (JsPath \ "FirstName").read[String] and
      (JsPath \ "Email").read[String] and
      (JsPath \ "ContactNumber").read[String] and
      (JsPath \ "Salary").read[Int])(EmployeeVO.apply _)
}
