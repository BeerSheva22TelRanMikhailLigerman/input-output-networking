package telran.net;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer implements Runnable {
private Protocol protocol;
private int port;
//CW54
private ExecutorService executor;

private ServerSocket serverSocket;

	@Override
	public void run() {
		System.out.println("Server listening on port " + this.port);
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				TcpServerClient serverClient = new TcpServerClient(socket, protocol);
				//serverClient.run();
				executor.execute(serverClient); //CW54
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	public TcpServer(Protocol protocol, int port) throws Exception{
		this.protocol = protocol;
		this.port = port;
		serverSocket = new ServerSocket(port);
		
		//CW54
		int nThreads = Runtime.getRuntime().availableProcessors();
		System.out.println("Number threads in Thread pool is: " + nThreads);
		executor = Executors.newFixedThreadPool(nThreads);		
	}
	public void shutdown() {
		//TODO
	}
}