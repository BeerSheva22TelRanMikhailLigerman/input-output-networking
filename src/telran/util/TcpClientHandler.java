package telran.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;

//HW40
public class TcpClientHandler implements Handler {
	//private static final String DEFAULT_HOSTNAME = "localhost";
	//private static final int DEFAULT_PORT = 4000;
	private static final String LOG_TYPE_REQUEST = "log";
	private static final String OK = "ok";
	Socket socket;
	PrintStream writerStream;	//stream
	BufferedReader readerStream; 	//input
		
	
	public TcpClientHandler(String hostName, int port) {		
		try {
			socket = new Socket(hostName, port);
			writerStream = new PrintStream(socket.getOutputStream());
			readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
		
	
	@Override
	public void publish(LoggerRecord loggerRecord) {	
		LocalDateTime ldt = LocalDateTime.ofInstant(loggerRecord.timestamp, ZoneId.of(loggerRecord.zoneId));
		String logMessageToServer = String.format("%s %s %s %s", ldt, loggerRecord.level, loggerRecord.loggerName, loggerRecord.message); // -\n
		writerStream.println(LOG_TYPE_REQUEST + "#" + logMessageToServer);
		try {
			String response = readerStream.readLine();
			if (!response.equals(OK)) {
				throw new RuntimeException("Response from Logger Server is " + response);
			}
		} catch (IOException e) {
			new RuntimeException(e.getMessage());
		}
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	


}
