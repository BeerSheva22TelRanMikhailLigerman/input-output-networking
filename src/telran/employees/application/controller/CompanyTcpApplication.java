package telran.employees.application.controller;

import telran.employees.Company;
import telran.employees.CompanyImpl;
import telran.employees.net.CompanyProtocol;
import telran.net.Protocol;
import telran.net.TcpServer;
//server part
//HW 45 my
public class CompanyTcpApplication {
	public static void main(String[] args) throws Exception {
		Company company = new CompanyImpl();
		company.restore("company.data");
		Protocol protocol = new CompanyProtocol(company);
		TcpServer server = new TcpServer(protocol, 4000);		
		server.run();	
	}
}
