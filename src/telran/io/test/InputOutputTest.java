package telran.io.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class InputOutputTest {

	String fileName = "myFile";
	String directoryName = "myDirectory1/myDirectory2";
	
	
	int indent = 3;
	
		@BeforeEach
		void setUp() throws Exception {
			new File(fileName).delete();
			new File(directoryName).delete();
		}

		@Test
		@Disabled
		void testFile() throws IOException {
			File f1 = new File(fileName);
			assertTrue(f1.createNewFile());
			File dir1 = new File(directoryName);
			assertTrue(dir1.mkdirs());
			System.out.println(dir1.getAbsolutePath());


		}
		//HW36
		@Test
		
		void printDirectoryFileTest() throws IOException {
			//String folder = "D:\\Java\\Tal-Ran Eclips\\folder_level1";
			String folder = "..";
			printDirectoryFile(folder, 2);
		}
		void printDirectoryFile(String path, int maxLevel) throws IOException {
			// based on class File
			//path -directory path
			//maxLevel - maximal level of printing, if maxLevel < 1, all levels should be printed
			//output format
			//  <directory name (no points, no full absolute path)
			//     <node name> - dir | file
			//          <node_name> .....
			//     <node name> -
			//          <node name> - dir | file
			//                <node_name> .....
			//     <node name> -
			if (maxLevel < 1) {
				maxLevel = Integer.MAX_VALUE;
			}
			
			File file = new File(path);
			System.out.println(file.getCanonicalFile().getName());			
			printDirectory(file.listFiles(), maxLevel, 1);
		}
		
		
		
		
		private void printDirectory(File[] listFiles, int maxLevel, int level) {
			if (level <= maxLevel) {
				Arrays.stream(listFiles).forEach(node -> {
					System.out.printf("%s%s - %s\n", " ".repeat(level * indent), node.getName(),
							node.isFile() ? "file" : "dir");
					if (node.isDirectory()) {
						printDirectory(node.listFiles(), maxLevel, level + 1);
					}
				});
			}
			
		}

		@Test
		@Disabled
		void printDirectoryFilesTest() throws IOException {
			//String folder = "D:\\Java\\Tal-Ran Eclips\\folder_level1";
			String folder = "..";
			printDirectoryFiles(folder, 2);
			
		}
		
		@SuppressWarnings("unchecked")
		void printDirectoryFiles(String folder, int maxLevel) throws IOException {
			// based on class Files
			//path -directory path
			//maxLevel - maximal level of printing, if maxLevel < 1, all levels should be printed
			//output format
			//  <directory name (no points, no full absolute path)
			//     <node name> - dir | file
			//          <node_name> .....
			//     <node name> -
			//          <node name> - dir | file
			//                <node_name> .....
			//     <node name> -
			if (maxLevel < 1) {
				maxLevel = Integer.MAX_VALUE;
			}
			Path path = Paths.get(folder);
			path = path.toAbsolutePath().normalize();
			Stream stream = Files.walk(path, maxLevel);
			int parentIndent = path.getNameCount();
			stream.forEach(obj -> System.out.println(stringObjobj(obj, parentIndent)));
		}

		private String stringObjobj(Object obj, int parentIndent) {
			Path path = (Path) obj;
			return getIndent(path, parentIndent) + path.getFileName() + " - " + (Files.isDirectory(path) ? "dir" : "file");
		}

		private String getIndent(Path path, int parentIndent) {
			
			return " ".repeat(indent * (path.getNameCount() - parentIndent));
		}

		

	
		

}
