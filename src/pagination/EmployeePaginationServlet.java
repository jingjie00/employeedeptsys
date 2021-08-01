package pagination;


import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.entity.DepartmentEmployee;
import model.entity.Employee;
import sessionbean.EmployeeSessionBeanLocal;
import utilities.LoggingGeneral;


@WebServlet("/EmployeePaginationServlet")
public class EmployeePaginationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private EmployeeSessionBeanLocal empbean;

	
	public EmployeePaginationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LoggingGeneral logger = (LoggingGeneral)request.getServletContext().getAttribute("log");
		logger.setEntryPoints(request);
		
		response.setContentType("text/html;charset=UTF-8");
		
		RequestDispatcher dispatcher = null;
		
		try {

			if(!PaginationValidate.multiplePageView(request, response))
				throw new Exception();
			
			int nOfPages = 0;
			int currentPage = (int) request.getAttribute("currentPage");
			int recordsPerPage = (int) request.getAttribute("recordsPerPage");
			String keyword = (String) request.getAttribute("keyword");
			String direction = (String) request.getAttribute("direction");
			
			int rows = empbean.getNumberOfRows(keyword);
			nOfPages = rows / recordsPerPage;
			if (rows % recordsPerPage != 0) {
				nOfPages++;
			}
			if (currentPage > nOfPages && nOfPages != 0) {
				currentPage = nOfPages; //if larger than total page, set to maximum
			}
			List<Employee> lists = empbean.readEmployee(currentPage, recordsPerPage,keyword,direction); //Ask bean to give list
			request.setAttribute("employees", lists);
			request.setAttribute("nOfPages", nOfPages);
			
			dispatcher = request.getRequestDispatcher("employee_view.jsp");
			logger.setContentPoints(request, "Success view");
		} catch (Exception ex) {
			dispatcher = request.getRequestDispatcher("error.jsp");
			logger.setContentPoints(request, "Unsuccess view" + ex.getMessage());
		} finally {
			dispatcher.forward(request, response);
			logger.setExitPoints(request);
		}
		
		logger.setContentPoints(request, "Success view");
		logger.setExitPoints(request);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}