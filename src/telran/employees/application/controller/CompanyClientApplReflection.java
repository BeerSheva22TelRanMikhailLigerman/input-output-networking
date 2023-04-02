package telran.employees.application.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;

import telran.employees.Company;
import telran.employees.cotroller.CompanyControllerItems;
import telran.employees.net.CompanyNetProxy;
import telran.net.*;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;
//HW46
public class CompanyClientApplReflection {
	//private static final String PACKAGE = "telran.employees.application.controller";
	private static final String PACKAGE = "telran.net";

	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		if (args.length == 0) {throw new RuntimeException("must be path to the config file");}
		properties.load(new FileInputStream(new File(args[0])));
		if (properties.size() < 4) {throw new RuntimeException("must be four properties");}
		
		String hostname = properties.getProperty("hostname");
		int port = Integer.parseInt(properties.getProperty("port"));
		String transport = properties.getProperty("transport") + "Client";
		String[] departments = properties.getProperty("departments").split(", ");
		
		Class<NetworkClient> clientClazz = (Class<NetworkClient>) Class.forName(PACKAGE + transport);
		Constructor<NetworkClient> constructor = clientClazz.getConstructor(String.class, int.class);
		NetworkClient client = constructor.newInstance(hostname, port);
		
		Company company = new CompanyNetProxy(client);
		
		Item[] itemsArr = CompanyControllerItems.getCompanyItems(company, departments);
		ArrayList<Item> items = new ArrayList<Item>();
		for (Item item: itemsArr) {items.add(item);}
		items.add(Item.of("Exit", io -> {
			try {
				((CompanyNetProxy) company).close();
			} catch (IOException e) {
				throw new RuntimeException(e.toString());
			}
		}, true));
		
		Menu menu = new Menu("Main menu", items);
		InputOutput io = new StandardInputOutput();
		menu.perform(io);
		
		io.writeLine("Thanks & Goodbye"); 
	}
}
