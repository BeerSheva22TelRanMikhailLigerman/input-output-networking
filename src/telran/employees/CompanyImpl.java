package telran.employees;

import java.io.*;
import java.util.*;
//HW39
public class CompanyImpl implements Company {
	private static final long serialVersionUID = 1L;
	
	private HashMap<Long, Employee> employeesMap = new HashMap<Long, Employee>();
	private HashMap<Integer, Set<Employee>> monthBirthMap = new HashMap<Integer, Set<Employee>>();
	private HashMap<String, Set<Employee>> departmentMap = new HashMap<String, Set<Employee>>();
	private TreeMap<Integer, Set<Employee>> salaryTree = new TreeMap<Integer, Set<Employee>>();
	

	@Override
	public Iterator<Employee> iterator() {		
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee empl) {
		boolean res = false;
		if (employeesMap.putIfAbsent(empl.id, empl) == null) {
			fillAnotherCollections(empl);
			res = true;
		}
		return res;
	}

	private void fillAnotherCollections(Employee empl) {
		addToMonthBirthMap(empl);
		addToDepartmentMap(empl);
		addToSalaryTree(empl);
		
	}

	private void addToSalaryTree(Employee empl) {
		if (!salaryTree.containsKey(empl.salary)) {
			salaryTree.put(empl.salary, new HashSet<Employee>()) ;
		} 
		salaryTree.get(empl.salary).add(empl);		
	}

	private void addToDepartmentMap(Employee empl) {
		if (!departmentMap.containsKey(empl.department)) {
			departmentMap.put(empl.department, new HashSet<Employee>()) ;
		} 
		departmentMap.get(empl.department).add(empl);		
	}	

	private void addToMonthBirthMap(Employee empl) {
		int monthBirth = empl.birthDate.getMonthValue();
		if (!monthBirthMap.containsKey(monthBirth)) {
			monthBirthMap.put(monthBirth, new HashSet<Employee>()) ;
		} 
		monthBirthMap.get(monthBirth).add(empl);		
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee delEmpl = employeesMap.remove(id);
		
		if (delEmpl != null) {
			departmentMap.get(delEmpl.department).remove(delEmpl);
			monthBirthMap.get(delEmpl.birthDate.getMonthValue()).remove(delEmpl);
			salaryTree.get(delEmpl.salary).remove(delEmpl);
		}
		return delEmpl;
	}

	@Override
	public List<Employee> getAllEmployees() {		
		return new ArrayList<>(employeesMap.values());
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {		
		return new ArrayList<>(monthBirthMap.getOrDefault(month, Collections.emptySet()));
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {		
		return  salaryTree.subMap(salaryFrom, true, salaryTo, true).values().stream().flatMap(Set::stream).toList();
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {		
		return new ArrayList<>(departmentMap.getOrDefault(department, Collections.emptySet())) ; 
	}

	@Override
	public Employee getEmployee(long id) {		
		return employeesMap.get(id);
	}

	@Override
	public void save(String pathName) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(pathName))){
			output.writeObject(employeesMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))){
			employeesMap = (HashMap<Long, Employee>) input.readObject();
			employeesMap.values().stream().forEach(empl -> fillAnotherCollections(empl)); 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public boolean isEquals(Company comp) {
		//TODO
		return false;
	}
	
	

}
