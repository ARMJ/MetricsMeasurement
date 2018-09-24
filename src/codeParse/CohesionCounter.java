package codeParse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class CohesionCounter {
//    CompilationUnit compilationUnit;
//    public CohesionCounter(CompilationUnit compilationUnit){
//        this.compilationUnit = compilationUnit;
//    }
//
//    public int lcom(){
//
//        ArrayList<String> classVariables = new ArrayList<>();
//        ArrayList<String> methods = new ArrayList<>();
//        compilationUnit.findAll(MethodDeclaration.class).forEach(methodDeclaration -> {
//              methods.add(methodDeclaration.getNameAsString());
//        });
//        compilationUnit.findAll(FieldDeclaration.class).forEach(fieldDeclaration -> {
//            for(VariableDeclarator fd : fieldDeclaration.getVariables()){
//                classVariables.add(fd.getNameAsString());
//            }
//
//        });
//
//        return 0;
//    }
    ArrayList<String> fields = new ArrayList<>();
    ArrayList<String> methods = new ArrayList<>();
    HashMap<String,ArrayList<String>> nodes = new HashMap<>();
    int cc = 0;

    public CohesionCounter(CompilationUnit cu) {
        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cl->{
            collectClassData(cl);
        });

        for(String a:fields) nodes.put(a,new ArrayList<>());
        for(String a:methods) nodes.put(a,new ArrayList<>());
//        System.out.println(fields);
//        System.out.println(methods);

        cu.findAll(MethodDeclaration.class).forEach(m->createMethodEdges(m));
//        System.out.println("Printing Graph....");
//        for(String k:nodes.keySet()) {
//            System.out.println(k+":"+nodes.get(k));
//        }
        countCohesion();

    }

    private void collectClassData(ClassOrInterfaceDeclaration cu) {
        cu.findAll(FieldDeclaration.class).forEach(fd ->{
            for(VariableDeclarator v:fd.getVariables()) {
                fields.add(v.getNameAsString());
            }
        });
        cu.findAll(MethodDeclaration.class).forEach(m ->{
            methods.add(m.getNameAsString());
        });
    }
    private void createMethodEdges(MethodDeclaration m) {
        ArrayList<String> locals = new ArrayList<>();
        NodeList<Parameter> params = m.getParameters();
        for(Parameter p:params) locals.add(p.getNameAsString());

        //locals.add("HALI");
        m.findAll(VariableDeclarator.class).forEach(v->{
            locals.add(v.getNameAsString());
        });

        //connect with other methods in this class
        m.findAll(MethodCallExpr.class).forEach(ne->{
            if(methods.contains(ne.getNameAsString())){
                if(ne.getNameAsString().equals(m.getNameAsString())) {} //self-loop
                else {
                    nodes.get(m.getNameAsString()).add(ne.getNameAsString());
                    nodes.get(ne.getNameAsString()).add(m.getNameAsString());
                }
            }
        });
        //connect with class vars
        m.findAll(NameExpr.class).forEach(ne->{
            if(fields.contains(ne.getNameAsString())) { //if this matches with any class var
                if(!locals.contains(ne.getNameAsString())) { //but not with local var
                    nodes.get(m.getNameAsString()).add(ne.getNameAsString());
                    nodes.get(ne.getNameAsString()).add(m.getNameAsString());
                }
            }
        });

        //all this.x kind of stmt can only refer to class vars,can't be caught by NameExpr
        m.findAll(FieldAccessExpr.class).forEach(fae->{
            String[] tmp = fae.toString().split("\\.");
            //Below,no need to check if tmp[1] -in fields
            if(tmp[0].equals("this")) {
                if (!nodes.containsKey(m.getNameAsString())) {
                    nodes.put(m.getNameAsString(), new ArrayList<>());
                }
                if (!nodes.containsKey(fae.getNameAsString())) {
                    nodes.put(fae.getNameAsString(), new ArrayList<>());
                }
                nodes.get(m.getNameAsString()).add(fae.getNameAsString());
                nodes.get(fae.getNameAsString()).add(m.getNameAsString());
            }
        });
    }

    private void countCohesion() {
        HashMap<String,Boolean> vis = new HashMap<>();
        for(String k:nodes.keySet()) vis.put(k,false);
        cc = 0;
        for(String k:nodes.keySet()) {
            if(!vis.get(k)) {
                dfs(k,vis);
                cc++;
            }
        }
        //System.out.print(cc + ", ");
    }

    private void dfs(String v,HashMap<String,Boolean> vis) {
        vis.put(v,true);
        for(String mate:nodes.get(v)) {
            if(!vis.get(mate)) dfs(mate,vis);
        }
    }

    public String toString(){
        return (cc + ", ");
    }
}
