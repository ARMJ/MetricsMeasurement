package forLoop;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import codeParse.FileFinder;

public class Main {
	public static void main(String[] args) throws IOException {
        FileFinder fileFinder = new FileFinder("Data");
        Collection<File> javaFiles = fileFinder.getFiles();
        //Collection<String> classNames = fileFinder.getClassNames();
        //System.out.println(javaFiles.size());
        
        FindForLoops ffl = new FindForLoops();
        

		PrintWriter printWriterForInit = new PrintWriter(new File("init_comp_client.csv"));
		PrintWriter printWriterForIncreament = new PrintWriter(new File("increament_comp_client.csv"));
		PrintWriter printWriterForCompare = new PrintWriter(new File("compare_comp_client.csv"));
        
        for(File file : javaFiles){
            CompilationUnit cu = JavaParser.parse(file);
            ffl.getForLoops(cu, file.getName());
            
            printWriterForInit.write(ffl.getInit());
            printWriterForCompare.write(ffl.getCompare());
            printWriterForIncreament.write(ffl.getIncreament());
        } 
        
        printWriterForInit.close();
        printWriterForCompare.close();
        printWriterForIncreament.close();
        
	}
}
