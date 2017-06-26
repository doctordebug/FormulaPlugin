import Utils.ClassVisitor;
import Utils.SootEnviroment;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import soot.Scene;
import soot.SootClass;
import soot.SootResolver;
import soot.util.Chain;
import ui.ResultPanel;

import java.util.Stack;

/**
 * Created by olisa_000 on 18.05.17.
 */
public class AnalyseAction extends AnAction implements Condition{


    private PsiFile astFile;

    public AnalyseAction(){
        super("Analyse Action");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if(e.getProject() == null) return;
        FileEditorManager manager = FileEditorManager.getInstance(e.getProject());
        VirtualFile files[] = manager.getSelectedFiles();
        //Any files open?
        if(files.length == 0 || !files[0].getPath().toLowerCase().endsWith(".java"))
            return;
        //Setup Soot
        if(!SootEnviroment.setup(files[0])) throw new IllegalArgumentException("Sth. stupid happened");
        analyse(e);
    }

    private void analyse(AnActionEvent e) {
        PsiFile ast = e.getData(LangDataKeys.PSI_FILE);

        SootEnviroment.activateConstantPropagation();


        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String fileName = vFile != null ? vFile.getName() : null;

        SootClass c = Scene.v().loadClassAndSupport(fileName.substring(0,fileName.indexOf('.')));
        Scene.v().loadNecessaryClasses();
        new ForAnalyser(c).analyse();

        SootEnviroment.disableConstantPropagation();
    }

    @Override
    public boolean value(Object o) {
        return true;
    }
}
