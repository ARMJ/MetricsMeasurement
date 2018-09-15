package codeParse;

import java.util.List;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class CodeParser {
	
	public void getSomeMetrics (String str, String className) {
		CompilationUnit compilationUnit = JavaParser.parse(str);
		InheritanceCounter inCount = new InheritanceCounter(compilationUnit, className);
		
		List <String> interfaces = inCount.interfaces;
		List <String> parent = inCount.superClasses;
		
		System.out.println("Interfaces: ");
		for (int i=0; i<interfaces.size(); i++) {
			System.out.println(interfaces.get(i));
		}
		
		if (parent.size() == 0) {
			System.out.println("No parent class.");
		}
		
		else {
			System.out.println("Parent class: ");
			for (int i=0; i<parent.size(); i++) {
				System.out.println(parent.get(i));
			}
		}
		
		LineCounter loc = new LineCounter(compilationUnit);
		System.out.println("Line of code: " + loc.getLineOfCodes());
		System.out.println("Line of comments: " + loc.lineOfComments);
		System.out.println("Comment Percentage: " + (loc.lineOfComments*100.0/loc.lineOfCode) + "%");
	}

	public static void main(String[] args) {
		String str = ""
				+ "public class A extends B implements C {\n"
				+ "		public int add (int x, int y) {\n"
				+ "			return x+y;\n"
				+ "		}\n"
				+ "		//Hello\n"
				+ "		/*This is \n"
				+ "		a multiline comment.*/\n"
				+ "		public int multiply (int x, int y) {\n"
				+ "			return x*y;\n"
				+ "		}\n"
				+ "		public int substract (int x, int y) {\n"
				+ "			return x-y;\n"
				+ "		}\n"
				+ "}\n";
		
		String className = "A";
		new CodeParser().getSomeMetrics(str, className);
	}
	

}
