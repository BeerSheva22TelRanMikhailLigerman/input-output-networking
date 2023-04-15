package git;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;


public class GitRepositoryImpl implements GitRepository {
	private static final long serialVersionUID = 1L;
	
	private String head = null;
	private HashMap<String, String> branches = new HashMap<>();
	private HashMap<String, Commit> commits = new HashMap<>();	
	private Set<String> ignoredExps = new HashSet<>();	
	
	String WORK_DIR = "c:\\myGitFolder";
	

	public static GitRepositoryImpl init() {
		GitRepositoryImpl res = null;
		if (!Files.exists(Path.of(GIT_FILE))) {
			res =  new GitRepositoryImpl();
		} else {
			try (var inputStream = new ObjectInputStream(new FileInputStream(GIT_FILE))){
				res = (GitRepositoryImpl) inputStream.readObject();
			} catch (Exception e) {
				System.out.println("Error git file reading");
			}
		}
		return res;
	}

	@Override
	public String commit(String commitMessage) {	//commitMessage - users's message
		List<FileState> fileStateList = info();

		if (!fileStateList.stream().anyMatch(fileState -> fileState.status != FileStatus.COMMITED)) {
			return "Nothing to commit";
		}
		CommitMessage commitMessageObj = new CommitMessage(commitMessage);
		LocalDateTime commitDateTime = LocalDateTime.now();
		Map<Path, FileParameters> fileParameters = null;
		try {
			fileParameters = getFileParameters(fileStateList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String prevCommitName = branches.get(head);
		
		Commit newCommit = new Commit(commitMessageObj, commitDateTime, fileParameters, prevCommitName);
		commits.put(newCommit.commitMessage().name, newCommit);
				
		if (head == null) {
			head = newCommit.commitMessage().name;
			createBranch("master");
		} else {
			branches.replace(head, newCommit.commitMessage().name);
		}
		return "Сommited successful";
	}

	private Map<Path, FileParameters> getFileParameters(List<FileState> fileStateList) throws IOException {
		Map<Path, FileParameters> res = new HashMap<>();
		for (FileState fileState: fileStateList) {
			if (!(fileState.status == FileStatus.COMMITED)) {
				res.put(fileState.path, new FileParameters(getFileData(fileState.path), Files.getLastModifiedTime(fileState.path).toInstant()));
			}
		}		
		return res;
	}

	private String[] getFileData(Path path) {
		try {
			return Files.readAllLines(path).toArray(String[]::new);
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	@Override
	public List<FileState> info() {
		Path directory = Path.of(WORK_DIR);
		try {
			return Files.walk(directory, 1).filter(path -> !mathesRegex(path.getFileName().toString()))
											.map(path -> new FileState(path, getStatus(path))).toList();
		} catch (IOException e) {
			System.out.println("Error during creation a list of FileStates" + e.getMessage());
			return null;
		}
	}

	
	private boolean mathesRegex(String fileName) {
		return ignoredExps.stream().anyMatch(fileName::matches);
	}

	private FileStatus getStatus(Path path)  {
		Commit commit = commits.getOrDefault(head, null);		
		if (commit == null || !commit.fileParameters().containsKey(path)) {
			return FileStatus.UNTRACKED;
		} else {			
			Instant fileLastModified = null;
			try {
				fileLastModified = Files.getLastModifiedTime(path).toInstant();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			Instant CommitFileLastModified = commit.fileParameters().get(path).timeOfFileModified();
			return  fileLastModified.isAfter(CommitFileLastModified)? FileStatus.MODIFIED : FileStatus.COMMITED;
		}
	}

	
	// 3
	@Override
	public String createBranch(String branchName) {
		if (head == null) {
			return "There is no Commits, need to create at least one commit first";
		} else if (branches.containsKey(branchName)) {
			return String.format("Branch  %s already exists", branchName);
		}
		branches.put(branchName, commits.get(head).commitMessage().name);
		head = branchName;
		return String.format("Branch %s has been created", branchName);
	}
	
	// 4
	@Override
	public String renameBranch(String branchName, String newName) {
		if (!branches.containsKey(branchName)) {
			return String.format("Branch  %s  finded", branchName);
		}
		if (branches.containsKey(newName)) {
			return String.format("Branch  %s already exists", newName);
		}		
		
		String commitName = branches.remove(branchName);
		branches.put(newName, commitName);
		if (head == branchName) {
			head = newName;
		}
		return String.format("Branch %s has been renamed to %s", branchName, newName);
	}
	
	// 5
	@Override
	public String deleteBranch(String branchName) {
		if (!branches.containsKey(branchName)) {
			return String.format("Branch  %s  not finded", branchName);
		}
		if (head == branchName) {
			return String.format(
					"Cann't delete current branch");
		}
		branches.remove(branchName);
		return String.format("Branch %s has been deleted", branchName);
	}

	// 6 return all commitMesages for current head
	@Override	
	public List<CommitMessage> log() {
		List<CommitMessage> res = new ArrayList<>();
		Commit commit = commits.get(branches.getOrDefault(head, head));
		while (commit != null) {
			res.add(commit.commitMessage());
			commit = commits.get(commit.prevCommitName());
		}
		return res;
	}
	
	// 7 returns List of branches with symbol "*" before current branch
	@Override
	public List<String> branches() {
		return branches.keySet().stream().map(name -> name == head ? "*" + name : name).toList();
	}
	
	// 8 returns List of files path from the commit
	@Override
	public List<Path> commitContent(String commitName) {
		return commits.get(commitName).fileParameters().keySet().stream().toList();
	}
	
	// 9
	@Override
	public String switchTo(String name) {
		if (name.equals(head)) {
			return String.format("%s is head now", name);
		}
		if (info().stream().anyMatch(fs -> !fs.status.equals(FileStatus.COMMITED))) {
			return "There are not commited files";
		}
		if (commits.get(branches.getOrDefault(name, name)) == null) {
			return "Branch or Commit not finded";
		}
		clearWorkDir();
		restoreFiles(name);
		head = name;
		return "Switched succusseful";
	}

	private void clearWorkDir() {
		try {
			Files.walk(Path.of(WORK_DIR)).filter(Files::isRegularFile).forEach(file -> { //TODO add ignored files filter
				try {
					Files.delete(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void restoreFiles(String name) {
		Commit commit = commits.get(branches.getOrDefault(name, name));
		commit.fileParameters().entrySet().stream().forEach(entry -> {
			try {
				Path path = entry.getKey();
				Instant dateOfModified = entry.getValue().timeOfFileModified();
				String[] data = entry.getValue().data();
			
				Files.createFile(path);
				Files.write(path, Arrays.asList(data));
				Files.setLastModifiedTime(path, FileTime.from(dateOfModified));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	// 11
	@Override
	public String getHead() {
		return head;
	}
	
	//12
	@Override
	public void save() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(GIT_FILE))) {
			output.writeObject(this);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
	
	// 13
	@Override
	public String addIgnoredFileNameExp(String regex) {
		String checker = "checker";
		try {
			checker.matches(regex);
			ignoredExps.add(regex);
		} catch (PatternSyntaxException e) {
			System.out.println("Incorrect regex");
		}
		return "Ignored string added";
	}
}
	
