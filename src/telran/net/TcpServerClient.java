package telran.net;
import java.io.*;
import java.net.*;

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
		while(running) {
			try {
				Request request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.writeObject(response);
			} catch (EOFException e) {
				System.out.println("client closed connection");
				running = false;
			} catch (Exception e)  {
				throw new RuntimeException(e.toString());
			}
		}

	}

}