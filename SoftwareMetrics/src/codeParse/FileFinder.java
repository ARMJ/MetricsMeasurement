package codeParse;

import java.util.Collection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileFinder {

	public FileFinder(String filePath) throws IOException {
		FileUtils fileutils = new FileUtils();
		File baseDirectory = new  File(filePath);
		String[] fileType = new String[1];
		
		fileType[0] = "java";
		Collection<File> javaFiles = FileUtils.listFiles(baseDirectory, fileType, true);
		System.out.println("Java Files: " + javaFiles.size());
		System.out.println();
		
		for (File file : javaFiles) {
			String classFileName = file.getName();
			String className = classFileName.replaceAll(".java", "");
			
			File read_file = new File(file.toString()); 
			  
			BufferedReader br = new BufferedReader(new FileReader(read_file)); 
			String codeLines = "";
			String st; 
			while ((st = br.readLine()) != null) {
				codeLines += st;
				codeLines += "\n";
			}
			
			System.out.println(classFileName);
			new CodeParser().getSomeMetrics(codeLines, className);
			System.out.println();
		}	
	}

	public static void main(String[] args) throws IOException {
		//new FileFinder("C:\\Users\\Eusha\\eclipse-workspace\\SoftwareMetrics2\\src");
		new FileFinder("C:\\Users\\Eusha\\Desktop\\JDeodorant-master");
	}

}
