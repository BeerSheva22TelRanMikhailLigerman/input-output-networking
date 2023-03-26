package telran.employees;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

class CompanyImplTest {
	CompanyImpl company; 
	Employee Vasa = new Employee(1, "Vasa", LocalDate.of(2000, 1, 10), "programmers", 10000);
	Employee Petya = new Employee(2, "Petya", LocalDate.of(2000, 1, 20), "programmers", 11000);
	Employee Masha = new Employee(3, "Masha", LocalDate.of(2000, 2, 10), "admin", 8000);

	@BeforeEach
	void setUp() throws Exception {	
		company = new CompanyImpl();
		company.addEmployee(Vasa);
		company.addEmployee(Petya);
		company.addEmployee(Masha);
	}	
	
	@Test
	//@Disabled
	void addRemoveTest() {		
		assertFalse(company.addEmployee(Vasa));
		assertEquals(Vasa, company.removeEmployee(1));
		assertNull(company.removeEmployee(1));
		assertTrue(company.addEmployee(Vasa));			
	}
	
	@Test
	//@Disabled
	void getAmployeeTest() {
		assertEquals(Vasa, company.getEmployee(1));
		assertNull(company.getEmployee(4));
	}
	
	@Test
	//@Disabled
	void getEmployeesByMonthBirthTest() {
		Employee[] expected = new Employee[] {Petya, Vasa};
		Employee[] actual = company.getEmployeesByMonthBirth(1).toArray(Employee[]::new);		
		Arrays.sort(actual, Comparator.comparing(Employee::getName));
		assertArrayEquals(expected, actual);
		
		assertTrue(company.getEmployeesByMonthBirth(3).isEmpty());
		
	}
	
	@Test
	//@Disabled
	void getAllEmployeeTest() {
		Employee[] expected = new Employee[] {Masha, Petya, Vasa};
		Employee[] actual = company.getAllEmployees().toArray(Employee[]::new);
		Arrays.sort(actual, Comparator.comparing(Employee::getName));
		assertArrayEquals(expected, actual);
		assertEquals(Vasa, company.getEmployee(1));
		assertNull(company.getEmployee(4));
	}
	
		
	@Test
	//@Disabled
	void getEmployeesBySalaryTest() {
		Employee[] expected = new Employee[] {Masha, Vasa};
		Employee[] actual = company.getEmployeesBySalary(8000, 10000).toArray(Employee[]::new);
		Arrays.sort(actual, Comparator.comparing(Employee::getName));
		assertArrayEquals(expected, actual);
	}
	
	@Test 
	//@Disabled
	void getEmployeesByDepartmentTest(){
		Employee[] expected = new Employee[] {Petya, Vasa};		
		Employee[] actual = company.getEmployeesByDepartment("programmers").toArray(Employee[]::new);
		Arrays.sort(actual, Comparator.comparing(Employee::getName));
		assertArrayEquals(expected, actual);
		
		company.removeEmployee(1);
		expected = new Employee[] {Petya};
		actual = company.getEmployeesByDepartment("programmers").toArray(Employee[]::new);
		assertArrayEquals(expected, actual);		
		
		assertTrue(company.getEmployeesByDepartment("emptyDep").isEmpty());
	}
	
	@Test
	@Disabled
	void saveTest() {
		company.save("company.data");
	}
	
	@Test
	//@Disabled
	void saveRestoreTest() {
		company.restore("company.data");
		assertIterableEquals(company, company);
					
		//assertIterableEquals(company, restoredCompany);
	}
	

}
