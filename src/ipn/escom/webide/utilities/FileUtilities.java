package ipn.escom.webide.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class FileUtilities {
	
	public static void create(String path) throws IOException {
		FileWriter fileWriter = new FileWriter(path);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.close();
        fileWriter.close();
	}
	
	public static void rename(String path, String newPath) {
		File file = new File(path);
		File newFile = new File(newPath);
	    file.renameTo(newFile);
	}
	
	public static void save(String path, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(content);
        bufferedWriter.close();
        fileWriter.close();
	}
	
	public static void delete(String path) {
		File file = new File(path);
		file.delete();
	}
	
	public static String getContent(String path) throws Exception {
		FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = "";
		String content = "";
		
        while((line = bufferedReader.readLine()) != null)
        	content += line + "\n";

        bufferedReader.close();
        fileReader.close();
        
        return content;
	}
	
	public static StreamedContent getStreamedContent(String path, String contentType, String fileName) throws IOException {
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        StreamedContent streamedContent = new DefaultStreamedContent(inputStream, contentType, fileName);
        
        return streamedContent;
	}

}
