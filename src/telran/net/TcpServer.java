package telran.net;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TcpServer implements Runnable {
private static final int TIMEOUT = 5_000;
private Protocol protocol;
private int port;
private ServerSocket serverSocket;
//CW54
private static boolean flagShutdown = false;
private ExecutorService executor;



	@Override
	public void run() {
		System.out.println("Server listening on port " + this.port);	
		while(!flagShutdown) {
			try {
				Socket socket = serverSocket.accept();
				socket.setSoTimeout(TIMEOUT);
				TcpServerClient serverClient = new TcpServerClient(socket, protocol);
				executor.execute(serverClient); //CW54
			} catch (SocketTimeoutException e) {
				if (flagShutdown) {					
					System.out.println("Server's been shutdown");
					break;
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				break;
			}
		}
		try {
			serverSocket.close();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	public TcpServer(Protocol protocol, int port) throws Exception{
		this.protocol = protocol;
		this.port = port;
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(TIMEOUT);
		
		//CW54
		int nThreads = Runtime.getRuntime().availableProcessors();
		System.out.println("Number threads in Thread pool is: " + nThreads);
		executor = Executors.newFixedThreadPool(nThreads);		
	}
	
	public void shutdown() {
		flagShutdown = true;
		executor.shutdownNow();  //why do not use awaitTermination ?		
	}
	
	public static boolean isFlShutdown() {
		return flagShutdown;
	}
}