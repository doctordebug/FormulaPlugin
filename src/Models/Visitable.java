package Models;

/**
 * Created by olisa_000 on 26.06.17.
 */
public interface Visitable {
    StringBuffer buildNodeRepresentation(StringBuffer sb);
    StringBuffer buildLatexNodeRepresentation(StringBuffer sb);
}
