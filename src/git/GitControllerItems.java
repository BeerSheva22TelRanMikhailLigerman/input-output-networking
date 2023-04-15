package git;

import telran.view.InputOutput;

public class GitControllerItems {
	private GitRepositoryImpl git;

	public GitControllerItems(GitRepositoryImpl git) {
		this.git = git;
	}

	public Menu menu() {
		Item commit = Item.of("commit", io -> commit(io));
		Item info = Item.of("info", io -> info(io));
		Item createBranch = Item.of("createBranch", io -> createBranch(io));
		Item renameBranch = Item.of("renameBranch", io -> renameBranch(io));
		Item deleteBranch = Item.of("deleteBranch", io -> deleteBranch(io));
		Item log = Item.of("log", io -> log(io));
		Item branches = Item.of("branches", io -> branches(io));
		Item commitContent = Item.of("commitContent", io -> commitContent(io));
		Item switchTo = Item.of("switchTo", io -> switchTo(io));
		Item getHead = Item.of("getHead", io -> getHead(io));
		Item addIgnoredFileNameExp = Item.of("addIgnoredFileNameExp", io -> addIgnoredFileNameExp(io));
		return new Menu("Menu", commit, info, createBranch, renameBranch, deleteBranch, log, branches, commitContent,
				switchTo, getHead, addIgnoredFileNameExp, Item.of("Exit", io -> {
					git.save();
					io.writeLine("Gitf has been saved");
				}, true));
	}

	private void commit(InputOutput io) {
		String commitMessage = io.readString("Enter commit message");
		io.writeLine(git.commit(commitMessage));
	}

	private void info(InputOutput io) {
		List<FileState> res = git.info();
		res.forEach(io::writeLine);
	}

	private void createBranch(InputOutput io) {
		String branchName = io.readString("Enter branch name");
		io.writeLine(git.createBranch(branchName));
	}

	private void renameBranch(InputOutput io) {
		String branchName = io.readString("Enter branch name which you want to rename");
		String newName = io.readString("Enter thw new name of the branch");
		io.writeLine(git.renameBranch(branchName, newName));
	}

	private void deleteBranch(InputOutput io) {
		String branchName = io.readString("Enter branch name which you want to delete");
		io.writeLine(git.deleteBranch(branchName));
	}

	private void log(InputOutput io) {
		List<CommitMessage> res = git.log();
		res.forEach(io::writeLine);
	}

	private void branches(InputOutput io) {
		List<String> res = git.branches();
		res.forEach(io::writeLine);
	}

	private void commitContent(InputOutput io) {
		String commitName = io.readString("Enter commit name");
		List<Path> res = git.commitContent(commitName);
		res.forEach(io::writeLine);
	}

	private void switchTo(InputOutput io) {
		Set<String> branchesNames = git.branches.keySet();
		Set<String> commitsNames = git.commits.keySet();

		String name = io.readString(String.format(
				"Enter branch or commit name on which you want to switch: /n One of the following branches: %s /n One of the following commits: %s",
				String.join(", ", branchesNames), String.join(", ", commitsNames)));
		io.writeLine(git.switchTo(name));
	}

	private void getHead(InputOutput io) {
		io.writeLine(String.format("Head equals %s", git.getHead()));
	}

	private void addIgnoredFileNameExp(InputOutput io) {
		String regex = io.readString("Enter regex for file names which will be ignored");
		io.writeLine(git.addIgnoredFileNameExp(regex));
	}

}
