package Utils;

import Analysis.ConstantPropagation;
import com.intellij.openapi.vfs.VirtualFile;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

/**
 * Created by olisa_000 on 18.05.17.
 */
public class SootEnviroment {

    //TODO: make singelton -> make options available

    public static boolean setup(VirtualFile fileToAnalyse){
        try {
            String dir =  fileToAnalyse.getParent().getPath() ;
            Options.v().setPhaseOption("jb", "use-original-names");
            Options.v().set_allow_phantom_refs(true);
            Options.v().set_soot_classpath( System.getProperty("java.home")+";"+ dir);
            return true;
        }catch(Exception e){
            ExceptionHandler.addException(e);
            return false;
        }
    }

    public static void activateConstantPropagation(){
        //make sure the methods are flat and as simple as possible
        if(PackManager.v().getPack("jtp").get("jtp.propagator") != null)
            PackManager.v().getPack("jtp").add(new Transform("jtp.propagator", ConstantPropagation.getInstance()));
    }

    public static void disableConstantPropagation(){
        if(PackManager.v().getPack("jtp").get("jtp.propagator") != null)
            PackManager.v().getPack("jtp").remove("jtp.propagator");
    }

}
