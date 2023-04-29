package telran.employees.application.controller;
import telran.employees.net.*;
import telran.net.*;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;
//HW 45 my
//Client part
public class CompanyClientAppl {

	public static void main(String[] args) throws Exception {
				
		CompanyNetProxy proxy = new CompanyNetProxy(new TcpClient("localhost", 4000));
		InputOutput io = new StandardInputOutput();
		
		CompanyControllerItems companyControllerItems = new CompanyControllerItems(proxy);
				
		Menu menu = new Menu("Company Menu",
				companyControllerItems.getAdminOperationMenu(),
				companyControllerItems.getUserOperationMenu(),				 
				Item.exit());
		menu.perform(io);
		
		io.writeLine("Thanks & Goodbye");
	}	
}
