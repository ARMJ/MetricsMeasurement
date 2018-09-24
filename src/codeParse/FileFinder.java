package codeParse;

import java.util.ArrayList;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.apache.commons.io.FileUtils;

public class FileFinder {

	private File baseDirectory;
	private Collection<File> javaFiles;

	public Collection<File> getFiles(){
		String[] fileType = new String[1];

		fileType[0] = "java";
		javaFiles = FileUtils.listFiles(baseDirectory, fileType, true);
//		System.out.println("Java Files: " + javaFiles.size());
//		System.out.println();
		return javaFiles;
	}

	public Collection<String> getClassNames() throws IOException {
		Collection<String> classNames = new ArrayList<>();
		for(File file : javaFiles){
			CompilationUnit cu = JavaParser.parse(file);
			cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cli -> {
				classNames.add(cli.getNameAsString());
			});
		}
		return classNames;
	}

	public FileFinder(String filePath) throws IOException {
		this.baseDirectory = new File(filePath);
	}


}
