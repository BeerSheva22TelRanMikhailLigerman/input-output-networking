package copyFiles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopyByFiles {
	public static void copyByFiles(String file, String fileCopy, boolean overWrite) throws Exception  {
		long timeStart = System.currentTimeMillis();
		
		try {			
			Path filePath = Paths.get(file);
			filePath = filePath.toAbsolutePath().normalize();
			if (!Files.exists(filePath)) {
				throw new FileNotFoundException();
			}
			Path filePathCopy = Paths.get(fileCopy);
			if (Files.exists(filePathCopy) && !overWrite) {
				throw new FileAlreadyExistsException(fileCopy);
			}			
			
			long fileSize = Files.size(filePath);
			Files.copy(filePath, filePathCopy);	
			System.out.print(fileSize + " bytes copyd, ");
			
		} catch (FileNotFoundException e) {
			System.out.println("No input File");
		} catch (FileAlreadyExistsException e) {
			System.out.println("Fail: cann't overwrite an existing file");
		}	
		
		System.out.println((System.currentTimeMillis() - timeStart)/1000 + " sec");
			
	}

}
