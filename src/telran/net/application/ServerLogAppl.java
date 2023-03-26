package telran.net.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import telran.util.Level;

public class ServerLogAppl {
	public static final String OK = "ok";
	public static final int PORT = 3000;
	public static final String LOG_TYPE = "log";
	public static final String COUNTER_TYPE = "counter";
	private static final String WRONG_LEVEL_MESSAGE = "Wrong Level in logger record";	
	
	static HashMap<Level, Integer> logLevelCounters = new HashMap<>();
	
	public static void main(String[] args) throws Exception{
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("server listening on port " + PORT );
			while(true) {
				Socket socket = serverSocket.accept();
				try {
					runServerClient(socket);
				} catch (IOException e) {
					System.out.println("abnormal closing connection");
				}
			}
		}

	}

	private static void runServerClient(Socket socket) throws IOException  {
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream writer = new PrintStream(socket.getOutputStream());
		while(true) {
			String request = reader.readLine();
			if (request == null) {
				break;
			}
			String response = getResponse(request);
			writer.println(response);
		}
		
	}
	

	private static String getResponse(String request) {
		String res = "Wrong Request";
		String tokens[] = request.split("#");
		if (tokens.length == 2) {
			res = switch(tokens[0]) {
			case LOG_TYPE -> processingLog(tokens[1]);
			case COUNTER_TYPE -> processingCounterRequest(tokens[1]); 
			default -> String.format("Wrong request type: should be either %s or %s", LOG_TYPE, COUNTER_TYPE);
			};
		}
		return res;
	}

	private static String processingCounterRequest(String logMessage) {
		
		return logLevelCounters.get(getLevel(logMessage)).toString();
	}

	private static String processingLog(String logMessage) {
		Level level = getLevel(logMessage);
		String res = WRONG_LEVEL_MESSAGE;
		if (level != null) {
			res = OK;
			//{
			if (!logLevelCounters.containsKey(level)) {
				logLevelCounters.put(level, 1);
			} else {
				int newValue = logLevelCounters.get(level) +1;
				logLevelCounters.put(level, newValue);
			}
			//}
			// more simple code fore {}:
			//logLevelCounters.merge(level, 1, (a,b) -> a + b);
		}
		return res;
		
	}

	private static Level getLevel(String logString) {
		Level levels[] = Level.values();
		boolean levelFound = false;
		int level = 0;
		for (int i = 0; i < levels.length; i++) {
			if (logString.contains((levels[i]).toString())) {
				levelFound = true;
				level = i;
			}
		}		
		return levelFound ? levels[level] : null;
	}
	// more simple code:
	//int index = 0;		
	//while(index < levels.length && !logString.contains(levels[index].toString())) {
	//index++;	}
			
}

