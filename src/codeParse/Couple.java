package codeParse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;


public class Couple {
	private Collection<String> classNames;
	private int coupleNumber = 0;
	
	public Couple(CompilationUnit cu, Collection<String> classNames) {
		this.classNames = classNames;
		cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cli -> {
			countFanOut(cli);
		});
		//System.out.print(coupleNumber + ", ");
	}
	
	private void countFanOut(ClassOrInterfaceDeclaration classDef) {
		HashSet<String> refs = new HashSet<>();
		classDef.findAll(ObjectCreationExpr.class).forEach(oce -> {	
			//System.out.println(oce.getType().getNameAsString());
			if(classNames.contains(oce.getType().getNameAsString())) {
				if(!oce.getType().getNameAsString().equals(classDef.getNameAsString())){
					refs.add(oce.getTypeAsString());
					//System.out.println(classDef.getNameAsString()+"->"+oce.getType().getNameAsString());
				}
			}
		});
		//System.out.println(classDef.getNameAsString()+ ": " +refs.size());
		coupleNumber = refs.size();
	}

	public String toString(){
		return (coupleNumber + ", ");
	}
}
