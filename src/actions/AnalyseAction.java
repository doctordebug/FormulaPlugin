package actions;

import Models.Loop;
import Utils.SootEnviroment;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiFile;
import soot.Scene;
import soot.SootClass;

import javax.swing.*;
import java.util.List;

/**
 * Created by olisa_000 on 18.05.17.
 */
public class AnalyseAction extends AnAction implements Condition{

    public static boolean isReady = false;

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
        SootClass c = Scene.v().loadClassAndSupport("Test");
        Scene.v().loadNecessaryClasses();
        List<ForAnalyser.AnalysisResult> result = new ForAnalyser(c).analyse();
        SootEnviroment.disableConstantPropagation();
        isReady = true;
        ToolWindow window = ToolWindowManager.getInstance(e.getProject()).registerToolWindow("FormulaPluginResultPanel", false, ToolWindowAnchor.RIGHT);
        ResultGui.showResult(window, result);


    }

    @Override
    public boolean value(Object o) {
        return isReady;
    }


}
