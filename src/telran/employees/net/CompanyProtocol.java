 package telran.employees.net;

import java.io.Serializable;
import java.util.List;

import telran.employees.Company;
import telran.employees.Employee;
import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;
//CW 43
public class CompanyProtocol implements Protocol {
	Company company;
	@Override
	public Response getResponse(Request request) {
		try {
		return switch(request.type) {
		case "addEmployee" -> getResponseData(addEmployee(request.data));
		case "removeEmployee" -> getResponseData(removeEmployee(request.data));
		case "getAllEmployees" -> getResponseData(getAllEmployees(request.data));
		case "getEmployeesByMonthBirth" -> getResponseData(getEmployeesByMonthBirth(request.data));
		case "getEmployeesBySalary" -> getResponseData(getEmployeesBySalary(request.data));
		case "getEmployeesByDepartment" -> getResponseData(getEmployeesByDepartment(request.data));
		case "getEmployee" -> getResponseData(getEmployee(request.data));
		case "save" -> getResponseData(save(request.data));
		case "restore" -> getResponseData(retsore(request.data));
		case "updateSalary" -> getResponseData(updateSalary(request.data));
		case "updateDepartment" -> getResponseData(updateDepartment(request.data));
		default -> new Response(ResponseCode.WRONG_REQUEST, request.type + " Request type not found");
		};
		}catch (Throwable e) {
			return new Response(ResponseCode.WRONG_DATA, e.toString());
		}
	}
	private Response getResponseData(Serializable data) {		
		return new Response(ResponseCode.OK, data);
	}
	Serializable addEmployee(Serializable data) {
		Employee empl = (Employee)data;
		return company.addEmployee(empl);
	}
	Serializable removeEmployee(Serializable data) {
		long id = (long) data;
		return company.removeEmployee(id);
	}
	Serializable getAllEmployees(Serializable data) {
		return (Serializable) company.getAllEmployees();
	}
	Serializable getEmployeesByMonthBirth(Serializable data) {
		int month = (int)data;
		return (Serializable) company.getEmployeesByMonthBirth(month);
	}
	Serializable getEmployeesBySalary(Serializable data) {
		int[] salaries = (int[]) data;
		return (Serializable) company.getEmployeesBySalary(salaries[0], salaries[1]);
	}
	Serializable getEmployeesByDepartment(Serializable data) {
		String department = (String)data;
		return (Serializable) company.getEmployeesByDepartment(department);
	}
	Serializable getEmployee(Serializable data) {
		long id = (long) data;
		return company.getEmployee(id);
	}
	Serializable save(Serializable data) {
		String filePath = (String)data;
		company.save(filePath);
		return "";
	}
	Serializable retsore(Serializable data) {
		String filePath = (String)data;
		company.restore(filePath);
		return "";
	}
	Serializable updateSalary(Serializable data) {
		Object[] newData = (Object[]) data; 
		long id = (long) newData[0];
		int newSalary = (int) newData[1];
		company.updateSalary(id, newSalary);
		return "";
	}
	
	Serializable updateDepartment(Serializable data) {
		Object[] newData = (Object[]) data; 
		long id = (long) newData[0];
		String department = (String) newData[1];
		company.updateDepartment(id, department);
		return "";
	}
	
	
	public CompanyProtocol(Company company) {
		this.company = company;
	}
	
	

}