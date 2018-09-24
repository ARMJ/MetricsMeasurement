package codeParse;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

class WeightedMethodForClass {
	public int complexity = 0;
	public int numberOfMethodsInClass = 0;
	public String methodToComplexity = "";

	public WeightedMethodForClass (CompilationUnit cu) {
		//System.out.print("[");
		methodToComplexity = "";
		complexity = 0;
		numberOfMethodsInClass = 0;
		for (TypeDeclaration typeDec : cu.getTypes()) {
			List<BodyDeclaration> members = typeDec.getMembers();
			if (members != null) {
				for (BodyDeclaration member : members) {
					if (member.isMethodDeclaration()) {
						MethodDeclaration field = (MethodDeclaration) member;
						int com = calculateCyclomaticComplexity(field.getNameAsString(), field.toString());
						//System.out.println("Method name: " + field.getNameAsString() + ", " + com.complexity);
						this.complexity += com;
						this.numberOfMethodsInClass += 1;
					}
				}
			}
		}

		//System.out.println("], " + complexity);
	}

	public int calculateCyclomaticComplexity(String methodName, String line) {
		int complexity = 1;
		String[] keywords = {"if", "while", "case", "for", "switch", "continue", "break", "?", "foreach" };
		String words = "";

		String [] str_arr = line.split("\n");
		for (int k=0; k<str_arr.length; k++) {
			String cline = str_arr[k];
			StringTokenizer stTokenizer = new StringTokenizer(cline);
			
			while (stTokenizer.hasMoreTokens()) {
				words = stTokenizer.nextToken();
				for (int i = 0; i < keywords.length; i++) {
					if (keywords[i].equals("\\")) {
						break;
					} 
					
					else if (keywords[i].equals(words)) {
						complexity++;
					}
				}
			}
		}
		methodToComplexity += (methodName + "->" + complexity + " #");
		//System.out.print(methodName + "->" + complexity + ", ");
		return (complexity);
	}

	public String toString(){
		return "[" + methodToComplexity + "], " + complexity + ", ";
	}
}