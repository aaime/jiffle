package org.jaitools.jiffle.parser.node;

/**
 *
 * @author michael
 */
public class SimpleStatement implements Statement {
    private final Expression expr;

    public SimpleStatement(Expression e) {
        this.expr = e;
    }

    @Override
    public String toString() {
        return expr + ";" ;
    }

    public void write(SourceWriter w) {
        w.indent();
        expr.write(w);
        w.append(";");
        w.newLine();
    }
}
