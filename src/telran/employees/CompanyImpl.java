package telran.employees;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.*;

public class CompanyImpl implements Company {
	
	private static final long serialVersionUID = 1L;
	private HashMap<Long, Employee> employees = new HashMap<>();
	private HashMap<Integer, Set<Employee>> employeesMonth = new HashMap<>();
	private HashMap<String, Set<Employee>> employeesDepartment = new HashMap<>();
	private TreeMap<Integer, Set<Employee>> employeesSalary = new TreeMap<>();
	
//	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//	Lock readLock = lock.readLock();
//	Lock writeLock = lock.writeLock(); 	 doesn't work
	
	@Override
	public Iterator<Employee> iterator() {		
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee empl) {
		boolean res = false;
		//writeLock.lock();
		if (addEmpl(empl)) {			
			addIndexMap(employeesMonth, empl.getBirthDate().getMonthValue(), empl);
			addIndexMap(employeesDepartment, empl.getDepartment(), empl);
			addIndexMap(employeesSalary, empl.getSalary(), empl);
			res = true;
		}		
		return res;
	}	

	private boolean addEmpl(Employee empl) {
		boolean res = false;
		//writeLock.lock();
		try {
			if (employees.putIfAbsent(empl.id, empl) == null) {
				res = true;
			} 
		} finally {
			//writeLock.unlock();
		}
		return res;
	}

	private <T> void addIndexMap(Map<T, Set<Employee>> map, T key, Employee empl) {
		//writeLock.lock();
		try {
			map.computeIfAbsent(key, k -> new HashSet<>()).add(empl);
		} finally {
			//writeLock.unlock();
		}
		
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee empl;
		//writeLock.lock();
		try {
			empl = employees.remove(id);
		} finally {
			//writeLock.unlock();
		} 
		if (empl != null) {
			removeIndexMap(employeesMonth, empl.getBirthDate().getMonthValue(), empl);
			removeIndexMap(employeesDepartment, empl.getDepartment(), empl);
			removeIndexMap(employeesSalary, empl.getSalary(), empl);
		}
		return empl;
	}

	private <T>void removeIndexMap(Map<T, Set<Employee>> map, T key, Employee empl) {
		//writeLock.lock();		
		try {
			Set<Employee> set = map.get(key);
			set.remove(empl);
			if (set.isEmpty()) {
				map.remove(key);
			} 
		} finally {
			//writeLock.unlock();
		}		
	}

	@Override
	public List<Employee> getAllEmployees() {	
		List<Employee> res;
		//readLock.lock();
		try {
			res = new ArrayList<>(employees.values());
		} finally {
		//	readLock.unlock();
		}
		return res;
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {	
		List<Employee> res;
		//readLock.lock();
		try {
			res = new ArrayList<>(employeesMonth.getOrDefault(month, Collections.emptySet()));
		} finally {
		//	readLock.unlock();
		}
		return res;
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {		
		List<Employee> res;
		//readLock.lock();
		try {
			res = employeesSalary.subMap(salaryFrom, true, salaryTo, true).values().stream().flatMap(Set::stream)
					.toList();
		} finally {
		//	readLock.unlock();
		}
		return res;
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		List<Employee> res;
		//readLock.lock();
		try {
			res = new ArrayList<>(employeesDepartment.getOrDefault(department, Collections.emptySet()));
		} finally {
		//	readLock.unlock();
		}
		return res;
	}

	@Override
	public Employee getEmployee(long id) {	
		Employee res;
		//readLock.lock();
		try {
			res = employees.get(id);
		} finally {
		//	readLock.unlock();
		}
		return res;
	}
	
	//two new methods for using ReentrantReadWriteLock (synchronize in all methods)
	@Override
	public void updateSalary(long emplId, int newSalary){
		Employee empl = getEmployee(emplId); 
		if (empl != null) {				
			removeIndexMap(employeesSalary, empl.getSalary(), empl);
			empl.setSalary(newSalary);
			addIndexMap(employeesSalary, empl.getSalary(), empl);
		}
	}
	
	@Override
	public void updateDepartment(long emplId, String department) {
		Employee empl = getEmployee(emplId); 
		if (empl != null) {				
			removeIndexMap(employeesDepartment, empl.getDepartment(), empl);
			empl.setDepartment(department);
			addIndexMap(employeesDepartment, empl.getDepartment(), empl);
		}
	}

	@Override
	public void save(String pathName) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(pathName))){
			output.writeObject(getAllEmployees());
		} catch(Exception e) {
			throw new RuntimeException(e.toString()); //some error
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))) {
			List<Employee> allEmployees = (List<Employee>) input.readObject();
			allEmployees.forEach(this::addEmployee);
		}catch(FileNotFoundException e) {
			//empty object but no error
		} catch (Exception e) {
			throw new RuntimeException(e.toString()); //some error
		}
	}
}