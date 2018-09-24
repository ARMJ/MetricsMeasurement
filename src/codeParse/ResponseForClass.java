package codeParse;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.ArrayList;
import java.util.HashSet;

public class ResponseForClass {

    HashSet<String> classMethods = new HashSet<>();
    HashSet<String> calledMethods = new HashSet<>();
    ArrayList<NodeList<VariableDeclarator>> classFields = new ArrayList<com.github.javaparser.ast.NodeList<com.github.javaparser.ast.body.VariableDeclarator>>();
    String instanceName;
    int rfc = 0;

    public ResponseForClass(CompilationUnit cu){
//        cu.findAll(FieldDeclaration.class).forEach(fieldDeclaration -> {
//            classFields.add(fieldDeclaration.getVariables());
//        });

        //System.out.println(classFields.toString());
        cu.findAll(MethodDeclaration.class).forEach(methodDeclaration -> {
            classMethods.add(methodDeclaration.getNameAsString());
            methodDeclaration.findAll(MethodCallExpr.class).forEach(methodCallExpr -> {
                instanceName = methodCallExpr.toString();
                //System.out.println(instanceName);

                if(instanceName.split("\\.").length > 1){
                    String [] arr = instanceName.split("\\.");

                    if(!(arr[0].equals("this"))) {
                        for (String s : arr){
                            if (s.contains("(")) {
                                calledMethods.add(s.split("\\(")[0]);
                            }
                        }
                    }
                }
            });
        });
        rfc = classMethods.size() + calledMethods.size();
        //System.out.print(rfc + ", ");

    }

    public String toString(){
        return (rfc + ", ");
    }
}
