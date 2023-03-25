package telran.employees;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable{
	
	public Employee(long id, String name, LocalDate birthDate, String department, int salary) {
		super();
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.department = department;
		this.salary = salary;
	}
	private static final long serialVersionUID = 1L;
	long id;
	String name;
	LocalDate birthDate;
	String department;
	int salary;
	
	public String getDepartment() {
		return department;
	}		
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public int getSalary() {
		return salary;
	}
	

}
