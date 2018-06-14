package org.jaitools.jiffle.parser.node;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author michael
 */
public class GlobalVars extends AbstractNode {
    private final List<BinaryExpression> inits;

    /**
     * Creates an empty instance.
     */
    public GlobalVars() {
        inits = Collections.emptyList();
    }

    /**
     * Creates an instance containing the given variables and optional 
     * initial values.
     */
    public GlobalVars(List<BinaryExpression> inits) {
        this.inits = inits;
    }

    @Override
    public void write(SourceWriter writer) {
        for (BinaryExpression init : inits) {
            writer.indent();
            init.write(writer);
            writer.append(";");
            writer.line();
        }
    }

    public void writeFields(SourceWriter w) {
        for (BinaryExpression init : inits) {
            init.writeDeclaration(w);
        }
    }
}
