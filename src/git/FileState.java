package git;

import java.io.ObjectInputFilter.Status;
import java.nio.file.Path;

public class FileState {
	Path path;
	FileStatus status;
	public FileState(Path path, FileStatus status) {		
		this.path = path;
		this.status = status;
	}
	
}
