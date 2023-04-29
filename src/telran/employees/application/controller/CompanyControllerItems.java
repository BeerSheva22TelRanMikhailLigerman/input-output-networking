package telran.employees.application.controller;

import static telran.view.Item.exit;
import static telran.view.Item.of;

import java.time.LocalDate;
import java.util.function.Predicate;

import telran.employees.Employee;
import telran.employees.net.CompanyNetProxy;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
//HW 45 my
public class CompanyControllerItems {
	CompanyNetProxy proxy;
			
	public CompanyControllerItems(CompanyNetProxy proxy) {
		this.proxy = proxy;
	}
	
	public Item getAdminOperationMenu() {		
		return new Menu("Administrative operations",
			of("Add employee", this::addEmployee),
			of("Remove employee", this::removeEmployee), 
			of("Update salary", this::updateSalary),
			of("Update department", this::updateDepartment),
			of("Save company", this::saveCompany),
			of("Restore company", this::restoreCompany),
			exit());
	}	
	void addEmployee(InputOutput io) {
		LocalDate minDate =  LocalDate.parse("1900-01-01");
		LocalDate maxDate =  LocalDate.now();
		Employee employee = new Employee(
				io.readInt("Enter ID", "must be a number", 0, Integer.MAX_VALUE),
				io.readString("Enter name"),		
				io.readDate("Enter birthDay", "must be a real date", "yyyy-MM-dd", minDate, maxDate),
				io.readString("Enter the department"),
				io.readInt("Enter the salary", "must be a positive number", 0, Integer.MAX_VALUE));		
		io.writeLine(proxy.addEmployee(employee));		
	}
	void removeEmployee(InputOutput io) {
		long id = io.readInt("Enter ID for remove", "must be a number", 0, Integer.MAX_VALUE);
		Employee removedEmployee = proxy.removeEmployee(id);
		if (removedEmployee == null) {io.writeLine("no such employee");}
		else {io.writeLine(removedEmployee);}		
	}
	
	void updateSalary(InputOutput io) {
		long id = io.readInt("Enter ID for update salary", "must be a number", 0, Integer.MAX_VALUE);
		int newSalary = io.readInt("Enter new salary", "must be a positive number", 0, Integer.MAX_VALUE);
		proxy.updateSalary(id, newSalary);		
	}
	
	void updateDepartment(InputOutput io) {
		long id = io.readInt("Enter ID for update department", "must be a number", 0, Integer.MAX_VALUE);
		String department = io.readString("Enter the department");
		proxy.updateDepartment(id, department);
	}
	
void saveCompany(InputOutput io) {
	Predicate<String> fileName = s -> s.matches("[\\w]+\\.[\\w]{1,4}"); 
	String filePath = io.readStringPredicate("Enter file name", "Must be a file name", fileName);
	proxy.save(filePath);		
	}
void restoreCompany(InputOutput io) {
	Predicate<String> fileName = s -> s.matches("[\\w]+\\.[\\w]{1,4}"); 
	String filePath = io.readStringPredicate("Enter file name", "Must be a file name", fileName);
	proxy.restore(filePath);
}

public Item getUserOperationMenu() {
	return new Menu("User Operations",
			of("Get all employees", this::getAllEmployees),
			of("Get employee", this::getEmployee),
			of("Get by department", this::getEmployeeByDepartment),
			of("Get by month of birth", this::getEmployeeByMonthBirth),
			of("Get by salary", this::getEmployeeBySalary),
			exit());
}
	void getAllEmployees(InputOutput io) {
		io.writeLine(proxy.getAllEmployees());
	}
	void getEmployee(InputOutput io) {
		long id = io.readInt("Enter ID", "must be a number", 0, Integer.MAX_VALUE);
		io.writeLine(proxy.getEmployee(id));
		}
	void getEmployeeByDepartment(InputOutput io) {
		String department = io.readString("Enter department");
		io.writeLine(proxy.getEmployeesByDepartment(department));
		}
	void getEmployeeByMonthBirth(InputOutput io) {
		int month = io.readInt("Enter month number", "Must be a namber from 1 to 12", 1, 12);
		io.writeLine(proxy.getEmployeesByMonthBirth(month));
		}
	void getEmployeeBySalary(InputOutput io) {
		int salaryMin = io.readInt("Enter salary from", "Must be a positive number", 0, Integer.MAX_VALUE);
		int salaryMax = io.readInt("Enter salary to", "Must be a positive number", 0, Integer.MAX_VALUE);
		io.writeLine(proxy.getEmployeesBySalary(salaryMin, salaryMax));
		}
}
