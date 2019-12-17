package ipn.escom.webide.utilities;

import static ipn.escom.webide.constants.SystemConstants.FILESEPARATOR;

import java.io.File;

public class CodeUtilities {
	
	public static String getCompilerCommand(String path, String language) throws Exception {
		int lastfileSeparator = path.lastIndexOf(FILESEPARATOR);
		String directory = path.substring(0, lastfileSeparator);
		String name = path.substring(lastfileSeparator + 1, path.lastIndexOf('.'));
		
		String command = "";
			
		if(language.equals("c")) {
			command = "gcc -o \"" + directory + FILESEPARATOR + name + ".exe\" \"" + path + "\"";
		} else if(language.equals("java")) {
            String code = FileUtilities.getContent(path);
            
            if(getPackageName(code) != null) {
            	command = getJavacPath() + "javac \"" + path + "\" -d \"" + directory + "\"";
			} else {
				command = getJavacPath() + "javac \"" + path + "\"";
			}
		}

		return command;
	}

	public static String getRunCommand(String path, String language) throws Exception {
		int lastfileSeparator = path.lastIndexOf(FILESEPARATOR);
		String directory = path.substring(0, lastfileSeparator);
		String name = path.substring(lastfileSeparator + 1, path.lastIndexOf('.'));

		String command = null;
		
		if(language.equals("c")) {
			command = "\"" + directory + FILESEPARATOR + name + ".exe" + "\"";
		} else if(language.equals("java")) {
			String code = FileUtilities.getContent(path);

			if(getPackageName(code) != null) {
				command = getJavacPath() + "java -cp \"" + directory + "\" " + getPackageName(code) + "." + name;
			} else {
				command = getJavacPath() + "java -cp \"" + directory + "\" \"" + name + "\"";
			}
		}
		
		return command;
	}
	
	public static void insertAutoflush(String path, String code) throws Exception {
		int mainIndex = code.indexOf("main");
		int keyIndex = code.indexOf("{", mainIndex + 4);

		String autoflush = "\n  setvbuf(stdout, NULL, _IONBF, 0);\n";
		String codeWithAutoflush = code.substring(0, keyIndex + 1) + autoflush + code.substring(keyIndex + 1, code.length());
		FileUtilities.save(path, codeWithAutoflush);
    }
	
    private static String getJavacPath(){
        String path = "";
        
        if(System.getProperty("os.name").indexOf("Windows") != -1){
            File parentFile = new File(System.getProperty("java.home")).getParentFile();
            
            for(File file : parentFile.listFiles()) {
                if(file.getName().startsWith("jdk"))
                    path = file.getPath() + FILESEPARATOR + "bin" + FILESEPARATOR;
            }
        }
        
        return path;
    }

    private static String getPackageName(String code) {
	    int initIndex = code.indexOf("package");
	    int endIndex = code.indexOf(";", initIndex + 8);
	    
	    if(initIndex != -1 && endIndex != -1)
	    	return code.substring(initIndex + 8, endIndex);
	    
	    return null;
    }
    
}
