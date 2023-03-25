package telran.io.objects;

import java.io.Serializable;

public class Person implements Serializable{	//Serializable - object to byte array

	private static final long serialVersionUID = 1L;	//only for version compatibility
	public long id;
	public String name;
	public Person person;
	public Person(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + "]";
	}


}