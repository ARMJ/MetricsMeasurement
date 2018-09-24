package codeParse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class Main {

    static String csv;
    public static void main(String[] args) throws IOException {
        FileFinder fileFinder = new FileFinder("Data");
        Collection<File> javaFiles = fileFinder.getFiles();
        Collection<String> classNames = fileFinder.getClassNames();

        System.out.println("Number of files: " + javaFiles.size());
        System.out.println("Number of classes: " + classNames.size());

        DepthInheritanceTree depthInheritanceTree = new DepthInheritanceTree(javaFiles, classNames);
        depthInheritanceTree.calDIT();

        PrintWriter printWriter = new PrintWriter(new File("result.csv"));
        csv = "Class Name, Line of Code, Line of Comments, RFC, Cohesion, Coupling, Cyclomatic Complexity, Weighted Methods, Depth of Class, Number of children\n";
        printWriter.write(csv);
        for(File file : javaFiles){
            CompilationUnit cu = JavaParser.parse(file);
            cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterfaceDeclaration -> {
                csv = "";
                csv += file.getName() + ", ";
                csv += new LineCounter(cu).toString();
                csv += new ResponseForClass(cu).toString();
                csv += new CohesionCounter(cu).toString();
                csv += new Couple(cu, classNames).toString();
                csv += new WeightedMethodForClass(cu).toString();
                csv += depthInheritanceTree.classDepth.get(classOrInterfaceDeclaration.getNameAsString()) + ", ";

                if(depthInheritanceTree.classToNumOfChild.containsKey(classOrInterfaceDeclaration.getNameAsString())){
                    csv += depthInheritanceTree.classToNumOfChild.get(classOrInterfaceDeclaration.getNameAsString());
                }
                else {
                    csv += "0";
                }
                csv += "\n";
                printWriter.write(csv);
            });
        }
        printWriter.close();
    }

}
