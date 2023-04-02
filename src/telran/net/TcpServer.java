package telran.net;
import java.io.IOException;
import java.net.*;

public class TcpServer implements Runnable {
private Protocol protocol;
private int port;
private ServerSocket serverSocket;

	@Override
	public void run() {
		System.out.println("Server listening on port " + this.port);
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				TcpServerClient serverClient = new TcpServerClient(socket, protocol);
				serverClient.run();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	public TcpServer(Protocol protocol, int port) throws Exception{
		this.protocol = protocol;
		this.port = port;
		serverSocket = new ServerSocket(port);
	}
}