package dao

import Model.{EmployeeTableDef, EmployeeVO}
import akka.japi.Option.some
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject
import scala.concurrent.Future

class EmployeeDAO @Inject() (protected val dbConfigProvider :DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{

  val employeeTable = TableQuery[EmployeeTableDef]

  def getAll : Future[Seq[EmployeeVO]] ={

     db.run(employeeTable.result)
  }

  def deleteEmployee(employeeId:Int) : Future[Int] ={
    db.run(employeeTable.filter(_.employeeId===employeeId).delete)
  }

  def addEmployee(employeeVO: EmployeeVO):Future[Int] ={
    db.run((employeeTable += employeeVO))
  }

  def updateEmployee(id:Int,employeeVO: EmployeeVO): Future[Option[EmployeeVO]] ={
    val newEmployee = employeeVO.copy(employeeId = Option(id))
    db.run(employeeTable.filter(_.employeeId === id).update(newEmployee)).map{
      case 0 => None
      case _ => some(newEmployee)
    }
  }
}
