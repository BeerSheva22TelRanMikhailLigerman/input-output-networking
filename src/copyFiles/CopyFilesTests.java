package copyFiles;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import copyFiles.*;

class CopyFilesTests {

	@BeforeEach
	void setUp() throws Exception {
	}

	
	@Test
	@Disabled
	void CopyByTransferTest() throws Exception {
		CopyByTransfer.copyTransfer("D:\\01.mp4", "D:\\02.mp4", true);
	}
	
	@Test
	@Disabled
	void copyByFilesTest() throws Exception {
		CopyByFiles.copyByFiles("D:\\01.mp4", "D:\\02.mp4", true);
	}
	
	@Test
	void copyWithBufferTest() throws Exception {
		CopyWithBuffer.copyWithBuffer("D:\\01.mp4", "D:\\02.mp4", true);
	}

}
