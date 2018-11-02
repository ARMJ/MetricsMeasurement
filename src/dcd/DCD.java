package dcd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

public class DCD {
	
	public void unusedVariableDetection(CompilationUnit cu){
		List<String> classVariables = new ArrayList<>();
		
		HashMap<String, ArrayList<String>> variablesInMethods = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> variablesInClass = new HashMap<String, ArrayList<String>>();
		HashMap<String, HashSet<String>> variablesAccessedInMethods = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> variablesAccessedInClass = new HashMap<String, HashSet<String>>();
		
		cu.findAll(ClassOrInterfaceDeclaration.class).forEach(coi -> {
			
			coi.findAll(FieldDeclaration.class).forEach(fd -> {
				for(VariableDeclarator v: fd.getVariables()){
					if(!variablesInClass.containsKey(coi.getNameAsString())){
						variablesInClass.put(coi.getNameAsString(), new ArrayList<String>());
					}
					variablesInClass.get(coi.getNameAsString()).add(v.getNameAsString());
				}
			});
			coi.findAll(MethodDeclaration.class).forEach(md -> {
				
				md.findAll(VariableDeclarationExpr.class).forEach(vd -> {
					for(VariableDeclarator v: vd.getVariables()){
						if(!variablesInMethods.containsKey(md.getNameAsString())){
							variablesInMethods.put(md.getNameAsString(), new ArrayList<>());
						}
						variablesInMethods.get(md.getNameAsString()).add(v.getNameAsString());
					}
				});
			});
			coi.findAll(NameExpr.class).forEach(ne -> {
				if(!variablesAccessedInClass.containsKey(coi.getNameAsString())){
					variablesAccessedInClass.put(coi.getNameAsString(), new HashSet<>());
				}
				variablesAccessedInClass.get(coi.getNameAsString()).add(ne.getNameAsString());
			});
			coi.findAll(MethodDeclaration.class).forEach(md -> {
				md.findAll(NameExpr.class).forEach(ne -> {
					if(!variablesAccessedInMethods.containsKey(md.getNameAsString())){
						variablesAccessedInMethods.put(md.getNameAsString(), new HashSet<>());
					}
					variablesAccessedInMethods.get(md.getNameAsString()).add(ne.getNameAsString());
				});
			});
			
		});
		
		int totalDeclared = 0;
		int totalAccessed = 0;
		
		for(String cls: variablesInClass.keySet()){
			totalDeclared += variablesInClass.get(cls).size();
			System.out.println(cls +  " -> declared = " + variablesInClass.get(cls).size() + ": " + variablesInClass.get(cls));
		}
		
		for(String mtd: variablesInMethods.keySet()){
			totalDeclared += variablesInMethods.get(mtd).size();
			System.out.println(mtd +  " -> declared = " + variablesInMethods.get(mtd).size() + ": " + variablesInMethods.get(mtd));
		}
		
		for(String cls: variablesAccessedInClass.keySet()){
			totalAccessed += variablesAccessedInClass.get(cls).size();
			System.out.println(cls +  " -> accessed = " + variablesAccessedInClass.get(cls).size() + ": " + variablesAccessedInClass.get(cls));
		}
		
		for(String mtd: variablesAccessedInMethods.keySet()){
			totalAccessed += variablesAccessedInMethods.get(mtd).size();
			System.out.println(mtd +  " -> accessed = " + variablesAccessedInMethods.get(mtd).size() + ": " + variablesAccessedInMethods.get(mtd));
		}
		
		System.out.println("declared: " + totalDeclared);
		System.out.println("accessed: " + totalAccessed);
		
//		cu.findAll(VariableDeclarationExpr.class).forEach(fd -> {
//			for(VariableDeclarator vd: fd.getVariables()){
//				classVariables.add(vd.getNameAsString());
//			}
//		});
//		System.out.println(classVariables.size());
//		int i = 1;
//		for(String v: classVariables){
//			System.out.println(i + ": " + v);
//			i++;
//		}
//		cu.findAll(MethodDeclaration.class).forEach(md -> {
//			System.out.print("method name: " + md.getNameAsString() + " =*= ");
////			System.out.println("method body: \n" + md.getBody().toString());
//			md.findAll(VariableDeclarationExpr.class).forEach(fd -> {
//				for(VariableDeclarator vd: fd.getVariables()){
//					variables.add(vd.getNameAsString());
//					System.out.print(vd.getNameAsString() + " - ");
//				}
//			});
//			System.out.println(variables.size());
//		});
	}
}
