package git;

import java.io.Serializable;
import java.util.Random;
import java.util.stream.Collectors;

public class CommitMessage implements Serializable {
	private static final long serialVersionUID = -6247898749051347696L;
	String name;
	String message;
	int nameLength = 7;

	public CommitMessage(String message) {
		//this.name = generateName(message); + correct method "generateName(String message)"
		this.name = generateName();  
		this.message = message;
	}

	private String generateName() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		return new Random().ints(nameLength, 0, chars.length()).mapToObj(chars::charAt).map(Object::toString)
				.collect(Collectors.joining());
	}
}
