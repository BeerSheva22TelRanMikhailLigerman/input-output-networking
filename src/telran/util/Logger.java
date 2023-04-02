package telran.util;

import java.time.Instant;
import java.time.ZoneId;

public class Logger {
	private static final Level DEFAULT_LEVEL = Level.INFO;
	private Level level = DEFAULT_LEVEL;	
	private Handler handler;
	private String name;
	
	public Logger(Handler handler, String name) {		
		this.handler = handler;
		this.name = name;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	private void publising(String message, Level level) {
		if (this.level.compareTo(level) <= 0) {
			LoggerRecord loggerRecord = createLoggerRecord(message, level);
			handler.publish(loggerRecord);
		}
	}
	private LoggerRecord createLoggerRecord(String message, Level level) {
		return new LoggerRecord(Instant.now(), ZoneId.systemDefault().toString(),
				level, name, message);
	}
	
	public void error(String message) {
		publising(message, Level.ERROR);

	}
	public void warn(String message) {
		publising(message, Level.WARN);

	}

	public void info(String message) {
		publising(message, Level.INFO);

	}
	public void debug(String message) {
		publising(message, Level.DEBUG);

	}
	public void trace(String message) {
		publising(message, Level.TRACE);
	}
}
