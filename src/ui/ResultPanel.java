package ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Created by olisa_000 on 26.06.17.
 */
public class ResultPanel implements ToolWindowFactory
{
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        Component component = toolWindow.getComponent();
        component.getParent().add(new JLabel("Hello, World!"));

    }
}
