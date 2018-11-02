package dcd;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class Main {

	public static void main(String[] args) throws IOException {
		FileFinder fileFinder = new FileFinder("Data");
        Collection<File> javaFiles = fileFinder.getFiles();
        Collection<String> classNames = fileFinder.getClassNames();
        
        DCD dcd = new DCD();
        
        System.out.println("Number of files: " + javaFiles.size());
        System.out.println("Number of classes: " + classNames.size());
        
        for(File file: javaFiles){
        	System.out.println(file.toString());
        	CompilationUnit cu = JavaParser.parse(file);
        	dcd.unusedVariableDetection(cu);
        	break;
        }

	}

}
