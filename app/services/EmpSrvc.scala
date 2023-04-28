package services

import Model.EmployeeVO
import dao.EmployeeDAO
import scala.concurrent.ExecutionContext.Implicits.global

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
@Singleton
class EmployeeService @Inject() (val employeeDAO: EmployeeDAO) {

  def getEmployee(company:String): Future[Seq[EmployeeVO]] = {
    employeeDAO.getAll.map(x=>x.filter(_.company==company))
  }

  def deleteEmployee(employeeID:Int):Future[Int] ={
    employeeDAO.deleteEmployee(employeeID)
  }

  def addEmployee(employeeVO: EmployeeVO):Future[Int]={
    employeeDAO.addEmployee(employeeVO)
  }

  def getEmployeeById(company:String,id:Int):Future[Option[EmployeeVO]]={
    employeeDAO.getAll.map(x=>x.filter(y => id==y.employeeId.get && y.company==company).headOption)
  }

  def update(company:String,id: Int, employee: EmployeeVO): Future[Option[EmployeeVO]] = {
    if(getEmployeeById(company, id).!=(null))
      {
        employeeDAO.updateEmployee(id, employee)
      }
    else
      {
        Future.apply(null)
      }
  }

}
