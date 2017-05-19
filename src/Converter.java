import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import soot.*;
import sun.nio.cs.ext.DoubleByte;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olisa_000 on 18.05.17.
 */
public class Converter {


    public static Type getSootType(PsiType type) {
        Type sootReturnType;
        switch (type.getPresentableText()){
            case "byte":
                sootReturnType = Type.toMachineType(ByteType.v());
                break;
            case "char":
                sootReturnType = Type.toMachineType(CharType.v());
                break;
            case "double":
                sootReturnType = Type.toMachineType(DoubleType.v());
                break;
            case "float":
                sootReturnType = Type.toMachineType(FloatType.v());
                break;
            case "int":
                sootReturnType = Type.toMachineType(IntType.v());
                break;
            case "long":
                sootReturnType = Type.toMachineType(LongType.v());
                break;
            case "short":
                sootReturnType = Type.toMachineType(ShortType.v());
                break;
            case "boolean":
                sootReturnType = Type.toMachineType(BooleanType.v());
                break;
            case "void":
                sootReturnType = Type.toMachineType(VoidType.v());
                break;
            default:
                sootReturnType = Type.toMachineType(NullType.v());

        }
        return sootReturnType;
    }

}
