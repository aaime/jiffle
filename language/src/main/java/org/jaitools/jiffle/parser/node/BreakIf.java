package org.jaitools.jiffle.parser.node;

public class BreakIf implements Statement {

    private final Expression condition;

    public BreakIf(Expression condition) {
        this.condition = condition;
    }

    public void write(SourceWriter w) {
        w.indent().append("if (_FN.sign(");
        condition.write(w);
        w.append(") == 1) break;").line();
    }
}
