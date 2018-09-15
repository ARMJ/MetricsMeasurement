package codeParse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class InheritanceCounter {
	List <String> superClasses = new ArrayList<String> ();
	List <String> interfaces = new ArrayList<String>();
	
	public InheritanceCounter (CompilationUnit cu, String className) {
		Optional<ClassOrInterfaceDeclaration> cd = cu.getClassByName(className);
		String classCode = cd.toString();
		
		String [] strArr = classCode.split("\\{");
		
		for (int i=0; i<strArr.length; i++) {
			if (strArr[i].contains(" class ") && strArr[i].contains(" " + className)) {
				String [] newStrArr = strArr[i].split(" class ");
				
				if (newStrArr.length > 1) {
					String classGolpo = newStrArr[newStrArr.length-1];
					if (classGolpo.contains(" implements ")) {
						String [] interfaces_str = classGolpo.split(" implements ")[1].split(" extends ")[0].split(",");
						for (int j=0; j<interfaces_str.length; j++) {
							interfaces.add(interfaces_str[j].replaceAll(" ", ""));
						}
					}
					
					if (classGolpo.contains(" extends ")) {
						String parent_str = classGolpo.split(" extends ")[1].split(" implements ")[0];
						superClasses.add(parent_str.replaceAll(" ", ""));
					}
				}
				break;
			}
			
		}
	}
}
