import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import soot.Scene;
import soot.SootClass;

import java.util.Stack;

/**
 * Created by olisa_000 on 18.05.17.
 */
public class AnalyseAction extends AnAction {

    public AnalyseAction(){
        super("Analyse Action");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        FileEditorManager manager = FileEditorManager.getInstance(e.getProject());
        VirtualFile files[] = manager.getSelectedFiles();
        //Any files open?
        if(files.length != 0)
            System.out.println(files[0]);
        //ist it a Java File//Class?
        if(!files[0].getPath().toLowerCase().endsWith(".java"))
            return;
        //Setup Soot
        if(!SootEnviroment.setup(files[0])) throw new IllegalArgumentException("Sth. stupid happend");
        analyse(e);
    }

    private void analyse(AnActionEvent e) {
        PsiFile ast = e.getData(LangDataKeys.PSI_FILE);
        ClassVisitor visitor = new ClassVisitor();
        ast.accept(visitor);
        Stack<String> stack = visitor.getClassNames();
        while(!stack.empty()){
            SootClass c = Scene.v().loadClassAndSupport(stack.pop());
            Scene.v().loadNecessaryClasses();
            new ForAnalyser(c).analyse();
        }
    }
}
