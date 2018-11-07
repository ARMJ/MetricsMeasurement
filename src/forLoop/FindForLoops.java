package forLoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.ForStmt;

public class FindForLoops {
	String initFileText = "";
	String compFileText = "";
	String increamentFileText = "";
	
	public void getForLoops(CompilationUnit cu, String fileName) throws FileNotFoundException {
		initFileText = "";
		compFileText = "";
		increamentFileText ="";
		
		
		//System.out.println(fileName);
		
		cu.findAll(ForStmt.class).forEach(fs -> {
			for(int i = 0; i < fs.getInitialization().size(); i++) {
				initFileText += fs.getInitialization().get(i).toString() + "\n";
			}
			
			//compFileText += fs.getCompare().get() + "\n";
			
			for(int i = 0; i < fs.getUpdate().size(); i++) {
				increamentFileText += fs.getUpdate().get(i).toString() + "\n";
			}
		});
	}
	
	public String getInit() {
		return this.initFileText;
	}
	
	public String getCompare() {
		return this.compFileText;
	}
	
	public String getIncreament() {
		return this.increamentFileText;
	}
	
}
