package Utils;

import com.intellij.psi.*;

import java.util.Stack;

/**
 * Created by olisa_000 on 18.05.17.
 */
public class ClassVisitor extends JavaRecursiveElementWalkingVisitor {
    private Stack<String> names = new Stack<>();

    @Override
    public void visitElement(PsiElement element) {
        super.visitElement(element);
        if (element instanceof PsiClass)
            this.names.push(((PsiClass)element).getName());
    }

    public Stack<String> getClassNames() {
        return names;
    }
}
