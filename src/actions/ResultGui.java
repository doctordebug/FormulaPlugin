package actions;

import Models.Formula;
import Models.Loop;
import com.intellij.openapi.wm.ToolWindow;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import soot.Value;
import soot.jimple.internal.JIfStmt;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

/**
 * Created by olisa_000 on 28.06.17.
 */
public class ResultGui {

    private static JComponent c;
    private static JPanel infoGrid;
    private static JPanel mainGrid;
    private static Loop loop;

    private static final int ROWS = 5;
    private static final int COLS = 2;
    private static JPanel[][] panelHolder;
    private static List<Formula> formula;

    public static void showResult(ToolWindow window, List<ForAnalyser.AnalysisResult> result) {
        c = window.getComponent();
        loop = result.get(0).loop;
        formula = result.get(0).formula;
        panelHolder = new JPanel[ROWS][COLS];
        initComponent(c);
    }

    private static void initComponent(JComponent c) {
        c.setLayout(new BorderLayout());
        mainGrid = new JPanel();
        infoGrid = new JPanel();
        mainGrid.setLayout(new BorderLayout());
        infoGrid.setLayout(new GridLayout(ROWS,COLS));

        for(int m = 0; m < ROWS; m++) {
            for (int n = 0; n < COLS; n++) {
                panelHolder[m][n] = new JPanel();
                infoGrid.add(panelHolder[m][n]);
            }
        }

        drawIndexVars(loop.getIndexVariable());
        drawAccuVars(loop.getAccumulationVariables());
        drawCoIndexVariables(loop.getCoIndexVariables());
        drawCondition(loop.getCondition());
        drawLoopend(loop.getLoopEnd());
        drawFormula(formula,mainGrid);

        mainGrid.add(infoGrid, BorderLayout.CENTER);
        c.add(mainGrid, BorderLayout.CENTER);
    }

    private static void drawFormula(List<Formula> formula, JPanel mainGrid) {
        Formula current = formula.get(0);
        if(current == null)
            return;

        TeXFormula form = new TeXFormula(current.getLatextRepresentation());
        TeXIcon ti = form.createTeXIcon(TeXConstants.STYLE_DISPLAY, 40);
        //UIUtil.createImageForGraphics(ti.getIconWidth(), ti.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR)
        BufferedImage b = new BufferedImage(ti.getIconWidth(), ti.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        ti.paintIcon(new JLabel(), b.getGraphics(), 0, 0);

        JLabel picLabel = new JLabel(new ImageIcon(b));


        mainGrid.add(picLabel,BorderLayout.NORTH);
    }


    private static void drawIndexVars(Value indexVariable) {
        if(indexVariable == null){
            panelHolder[0][0].add(new JLabel("No IndexVariable Variable found!"));
            panelHolder[0][1].add(new JLabel(""));
            return;
        }
        panelHolder[0][0].add(new JLabel("Indexvariable :"));
        panelHolder[0][1].add(new JLabel(indexVariable.toString()));
    }

    private static void drawAccuVars( Collection<Value> accumulationVariables) {
        if(accumulationVariables.isEmpty()){
            c.add(new JLabel("No Accumulation Variables found!"));
            return;
        }
        panelHolder[1][0].add(new JLabel("Accumulations Variables :"));
        panelHolder[1][1].add(new JLabel(accumulationVariables.toString()));
    }

    private static void drawCoIndexVariables( Collection<Value> coIndexVariables) {
        if(coIndexVariables.isEmpty()){
            c.add(new JLabel("No CoIndexVariables Variables found!"));
            return;
        }
        panelHolder[2][0].add(new JLabel("CoIndexVariables  :"));
        panelHolder[2][1].add(new JLabel(coIndexVariables.toString()));
    }

    private static void drawCondition(JIfStmt condition) {
        if(condition == null){
            panelHolder[3][0].add(new JLabel("No condition found!"));
            panelHolder[3][1].add(new JLabel(""));
            return;
        }
        panelHolder[3][0].add(new JLabel("Condition :"));
        panelHolder[3][1].add(new JLabel(condition.toString()));
    }


    private static void drawLoopend(Value loopEnd) {
        if(loopEnd == null){
            panelHolder[4][0].add(new JLabel("No loppend Variable found!"));
            panelHolder[4][1].add(new JLabel(""));
            return;
        }
        panelHolder[4][0].add(new JLabel("Loopend :"));
        panelHolder[4][1].add(new JLabel(loopEnd.toString()));
    }
}
