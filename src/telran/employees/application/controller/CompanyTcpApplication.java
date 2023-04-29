package telran.employees.application.controller;

import java.time.LocalDateTime;
import java.util.Scanner;

import telran.employees.Company;
import telran.employees.CompanyImpl;
import telran.employees.net.CompanyProtocol;
import telran.net.Protocol;
import telran.net.TcpServer;
//server part
//HW 45 my
public class CompanyTcpApplication {
	private static final String FILE_NAME = "company.data";

	public static void main(String[] args) throws Exception {
		Company company = new CompanyImpl();
		company.restore(FILE_NAME);
		Protocol protocol = new CompanyProtocol(company);
		TcpServer server = new TcpServer(protocol, 4000);		
		//CW54
		Thread thread = new Thread(server);
		thread.start();
		Scanner scanner = new Scanner(System.in);
		boolean running = true;
		while(running) {
			System.out.println("For stopping server enter command 'shutdown'");
			String line = scanner.nextLine();
			if (line.equals("shutdown")) {
				server.shutdown();
				thread.join();
				company.save(FILE_NAME);
				System.out.println(LocalDateTime.now() + " company was saved");
				running = false;
			}
		}
		System.out.println("Server stopped");	
	}
}
