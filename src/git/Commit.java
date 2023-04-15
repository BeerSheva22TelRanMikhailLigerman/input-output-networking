package git;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;

public record Commit(
		CommitMessage commitMessage, 
		LocalDateTime timeOfCommit, 
		Map<Path, FileParameters> fileParameters, 
		String prevCommitName)
		implements Serializable {
}
