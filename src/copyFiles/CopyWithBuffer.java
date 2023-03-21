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

public class CopyWithBuffer {
	private static final int DEFOULT_BUFFER_SIZE = 1_000_000;

	public static void copyWithBuffer(String file, String fileCopy, boolean overWrite) throws Exception {
		copyWithBuffer(file, fileCopy, overWrite, DEFOULT_BUFFER_SIZE);
	}
	
	public static void copyWithBuffer(String file, String fileCopy, boolean overWrite, int bufferSize) throws Exception  {
		long timeStart = System.currentTimeMillis();
		
		try (InputStream input = new FileInputStream(file);
			OutputStream output = new FileOutputStream(fileCopy);){			
			Path filePath = Paths.get(file);
			filePath = filePath.toAbsolutePath().normalize();
			if (!Files.exists(filePath)) {
				throw new FileNotFoundException();
			}
			Path filePathCopy = Paths.get(fileCopy);
			if (Files.exists(filePathCopy) && !overWrite) {
				throw new FileAlreadyExistsException(fileCopy);
			}			
			int copyCount = 0;
			long fileSize = Files.size(filePath);
			byte[] buffer = new byte[bufferSize];
			while (input.available() > bufferSize) {
				buffer = input.readNBytes(bufferSize);
				output.write(buffer);
				copyCount++;
			}
			if (input.available() > 0) {
				byte[] lastBuffer = new byte[input.available()];
				output.write(lastBuffer);
				copyCount++;
			}
			
			System.out.print(fileSize + " bytes copyd, by " + copyCount + ", ");
			
		} catch (FileNotFoundException e) {
			System.out.println("No input File");
		} catch (FileAlreadyExistsException e) {
			System.out.println("Fail: cann't overwrite an existing file");
		}	
		
		System.out.println((System.currentTimeMillis() - timeStart)/1000 + " sec");
			
	}


}
