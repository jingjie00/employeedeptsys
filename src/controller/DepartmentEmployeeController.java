package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.postgresql.util.PSQLException;

import com.ibm.wsdl.util.StringUtils;

import model.entity.DepartmentEmployee;
import model.entity.Employee;
import model.usebean.DepartmentEmployeeUseBean;
import model.usebean.DepartmentUseBean;
import model.usebean.EmployeeUseBean;
import model.entity.Employee;
import sessionbean.DepartmentEmployeeSessionBeanLocal;
import sessionbean.EmployeeSessionBeanLocal;
import utilities.ControllerManagement;
import utilities.LoggingGeneral;

@WebServlet("/DepartmentEmployeeController")
public class DepartmentEmployeeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
	private DepartmentEmployeeSessionBeanLocal deptempbean;

	public DepartmentEmployeeController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = (String) request.getAttribute("action");

		try {
			if (action.compareTo("add") == 0) {
				request.getSession().setAttribute("deub", new DepartmentEmployeeUseBean());
				RequestDispatcher req = request.getRequestDispatcher("departmentemployee_add.jsp");
				req.forward(request, response);
			} else if (action.compareTo("update") == 0) {
				request.getSession().setAttribute("deub", new DepartmentEmployeeUseBean());
				DepartmentEmployee deptemp = deptempbean.findDepartmentEmployee(request.getParameter("dept_id"),
						Long.valueOf(request.getParameter("emp_id")));
				request.getSession().setAttribute("deub", new DepartmentEmployeeUseBean(deptemp));
				RequestDispatcher req = request.getRequestDispatcher("departmentemployee_update.jsp");
				req.forward(request, response);
			} else {
				DepartmentEmployee deptemp = deptempbean.findDepartmentEmployee(request.getParameter("dept_id"),
						Long.valueOf(request.getParameter("emp_id")));
				request.getSession().setAttribute("deub", new DepartmentEmployeeUseBean(deptemp));
				RequestDispatcher req = request.getRequestDispatcher("departmentemployee_delete.jsp");
				req.forward(request, response);
			}

		} catch (EJBException ex) {
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LoggingGeneral logger = (LoggingGeneral) request.getServletContext().getAttribute("log");
		logger.setEntryPoints(request);

		String action = (String) request.getAttribute("action");

		if (action.compareTo("add") == 0) {
			DepartmentEmployeeUseBean deub = new DepartmentEmployeeUseBean();
			try {
				deub.setDept_id(request.getParameter("dept_id"));
				deub.setEmp_id(request.getParameter("emp_id"));
				deub.setFrom_date(request.getParameter("from_date"));
				deub.setTo_date(request.getParameter("to_date"));

				if (deub.validate()) {
					deptempbean.addDepartmentEmployee(deub);
					ControllerManagement.navigateJS(request,response);
					logger.setContentPoints(request,
							"Success " + action + " --> ID:" + deub.getDept_id() + " | " + deub.getEmp_id().toString());
					return;
				}
				deub.setOverall_error("Please fix the error below");
			} catch (Exception e) {
				errorRedirect(e, deub);
				logger.setContentPoints(request, e.getMessage());
			}
			request.getSession().setAttribute("deub", deub);
			RequestDispatcher dispatcher = request.getRequestDispatcher("departmentemployee_add.jsp");
			dispatcher.forward(request, response);

		} else if (action.compareTo("delete") == 0) {
			DepartmentEmployeeUseBean deub = new DepartmentEmployeeUseBean();
			try {
				deub.setDept_id(request.getParameter("dept_id"));
				deub.setEmp_id(request.getParameter("emp_id"));
				if (deptempbean.deleteDepartmentEmployee(deub)) {
					ControllerManagement.navigateJS(request,response);
					logger.setContentPoints(request, "Success " + action + " --> ID:" + deub.getDept_id());
					return;
				} else {
					deub.setDept_id_error("Department not exist");
					deub.setEmp_id_error("Employee not exist");
					deub.setOverall_error("Please fix the error below");
				}
				
			} catch (Exception e) {
				deub=new DepartmentEmployeeUseBean(deptempbean.findDepartmentEmployee(deub.getDept_id(),deub.getEmp_id()));
				errorRedirect(e, deub);
			}
			request.getSession().setAttribute("deub", deub);
			RequestDispatcher dispatcher = request.getRequestDispatcher("departmentemployee_delete.jsp");
			dispatcher.forward(request, response);

		} else if (action.compareTo("update") == 0) {
			DepartmentEmployeeUseBean deub = new DepartmentEmployeeUseBean();
			try {
				deub.setDept_id(request.getParameter("dept_id"));
				deub.setEmp_id(request.getParameter("emp_id"));
				deub.setFrom_date(request.getParameter("from_date"));
				deub.setTo_date(request.getParameter("to_date"));

				if (deub.validate()) {
					deptempbean.updateDepartmentEmployee(deub);
					ControllerManagement.navigateJS(request,response);
					logger.setContentPoints(request,
							"Success " + action + " --> ID:" + deub.getDept_id() + " | " + deub.getEmp_id().toString());
					return;
				}
				deub.setOverall_error("Please fix the error below");
			} catch (Exception e) {
				errorRedirect(e, deub);
				logger.setContentPoints(request, e.getMessage());
			}
			
			request.getSession().setAttribute("deub", deub);
			RequestDispatcher dispatcher = request.getRequestDispatcher("departmentemployee_delete.jsp");
			dispatcher.forward(request, response);

		}

	}

	public void errorRedirect(Exception e, DepartmentEmployeeUseBean deub) {

		PSQLException psqle = ControllerManagement.unwrapCause(PSQLException.class, e);
		if (psqle != null) {
			if (psqle.getMessage().contains("duplicate key value violates unique constraint")) {
				deub=new DepartmentEmployeeUseBean(deptempbean.findDepartmentEmployee(deub.getDept_id(),deub.getEmp_id()));
				deub.setOverall_error("Duplicate error. Please change the input as annotated below");
				if (psqle.getMessage().contains("primary")) {
					deub.setDept_id_error("Duplicate combination of department id and employee id");
					deub.setEmp_id_error("Duplicate combination of department id and employee id");
				} else
					deub.setOverall_error(psqle.getMessage());
				return;
			}else if(psqle.getMessage().contains("violates foreign key constraint")) {
				deub.setOverall_error("No related records. Please change the input as annotated below");
				if (psqle.getMessage().contains("\"department\"")) {
					deub.setDept_id_error("Department ID not exist in department table");
					deub.setExpress("department");
				}else if(psqle.getMessage().contains("\"employee\"")) {
					deub.setEmp_id_error("Employee ID not exist in employee table");
					deub.setExpress("employee");
				}else
					deub.setOverall_error(psqle.getMessage());
			}
		}else
			deub.setOverall_error(e.toString());
	}


}