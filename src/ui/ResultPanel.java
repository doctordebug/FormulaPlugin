package ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import actions.AnalyseAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static java.lang.Thread.sleep;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Created by olisa_000 on 26.06.17.
 */
public class ResultPanel implements ToolWindowFactory
{
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        System.err.println("Call of create");
        Component component = toolWindow.getComponent();

        component.getParent().add(new JLabel("test"));

        startMonitor(component);

    }

    @Override
    public void init(ToolWindow window) {
        Component component = window.getComponent();

        component.getParent().add(new JLabel("test"));

    }

    //TODO: usw notify and stuf
    private void startMonitor(Component component) {
        Runnable listener = () -> {
            while (!AnalyseAction.isReady){
                System.err.print(".");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            repaint(component);
        };
        new Thread(listener).start();
    }

    private void repaint(Component component){
        System.err.println("repaint");
        component.getParent().add(new JLabel("test"));
    }

}
