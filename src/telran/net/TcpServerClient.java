package telran.net;
import java.io.*;
import java.net.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class TcpServerClient implements Runnable{
	
private Socket socket;
private ObjectInputStream input;
private ObjectOutputStream output;
private Protocol protocol;


public TcpServerClient(Socket socket, Protocol protocol) throws IOException {
	this.protocol = protocol;
	this.socket = socket;	
	input = new ObjectInputStream(socket.getInputStream());
	output = new ObjectOutputStream(socket.getOutputStream());

}
	@Override
	public void run() {	
		boolean running = true;
		while(running && !TcpServer.isFlShutdown()) {
			try {
				Request request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.reset();
				output.writeObject(response);
			} catch (SocketTimeoutException e) {
				if (TcpServer.isFlShutdown()) {
					try {
						socket.close();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					running = false;
				}
			} catch (EOFException e) {
				System.out.println("client closed connection");
				running = false;
			} catch (Exception e)  {
				throw new RuntimeException(e.toString());
			}
		}
		
	}
}