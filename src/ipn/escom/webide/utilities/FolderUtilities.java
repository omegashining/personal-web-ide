package ipn.escom.webide.utilities;

import java.io.File;

public class FolderUtilities {
	
	public static void create(String path) {
		File folder = new File(path);
	    folder.mkdir();
	}
	
	public static void rename(String path, String newPath) {
		File file = new File(path);
		File newFile = new File(newPath);
	    file.renameTo(newFile);
	}

}
