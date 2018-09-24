package codeParse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DepthInheritanceTree {
    Collection<File> javaFiles;
    ArrayList<List<String>> parentChild = new ArrayList<>();
    Collection<String> classNames;
    ArrayList <String> parentNames = new ArrayList<>();
    HashMap<String,ArrayList<String>> parToChilds;
    public HashMap<String, Integer> classToNumOfChild = new HashMap<>();
    public HashMap <String, Integer> classDepth = new HashMap<>();


    public DepthInheritanceTree(Collection<File> javaFiles, Collection<String> classNames){
        this.javaFiles = javaFiles;
        this.classNames = classNames;
    }

    public void calDIT() throws IOException {
        for(File file : javaFiles){
            CompilationUnit cu = JavaParser.parse(file);
            cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cli -> {
                NodeList<ClassOrInterfaceType> parent = cli.getExtendedTypes();
                if(parent.size() > 0 ){
                    String parentName = parent.get(0).getNameAsString();
                    boolean flag = false;

                    for (String cName : classNames){
                        if(cName.equals(parentName)){
                            parentChild.add(Arrays.asList(parent.get(0).getNameAsString(), cli.getNameAsString()));
                            parentNames.add(parent.get(0).getNameAsString());
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        classDepth.put(cli.getNameAsString(), 1);
                    }
                }

                else classDepth.put(cli.getNameAsString(), 0);
            });
        }
        connectedNodes ();
    }

    public void connectedNodes () {
        HashMap<String,String> childToPar = new HashMap<>();
        parToChilds = new HashMap<>();
        for(int i=0;i<parentChild.size();i++){
            String par = parentChild.get(i).get(0);
            String child = parentChild.get(i).get(1);
            childToPar.put(child,par);
            if(!parToChilds.containsKey(par)) parToChilds.put(par,new ArrayList<>());
            parToChilds.get(par).add(child);
        }
        for(String child:childToPar.keySet()){
            String curChild = child;
            int depth = 0;
            while(childToPar.containsKey(child)){
                depth++;
                child = childToPar.get(child);
            }
            classDepth.put(curChild, depth);
        }
        for(String parent : parToChilds.keySet()){
            classToNumOfChild.put(parent, parToChilds.get(parent).size());
            //System.out.println(parent + ": " + parToChilds.get(parent).size());
        }

        System.out.println(classDepth.size());
//        for (String key : classDepth.keySet()){
//            System.out.println(key + ": " + classDepth.get(key));
//            if (classDepth.get(key) > 10) {
//                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
//            }
//        }
    }
}
