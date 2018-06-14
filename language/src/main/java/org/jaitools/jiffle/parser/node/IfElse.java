package org.jaitools.jiffle.parser.node;

public class IfElse implements Statement {

    private final Expression condition;
    private final Node ifStatement;
    private final Node elseStatement;

    public IfElse(Expression condition, Node ifStatement, Node elseStatement) {
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
        this.condition = condition;
    }

    @Override
    public void write(SourceWriter w) {
        w.indent().append("if (_FN.isTrue(").append(condition).append(")) {").line();
        w.inc();
        ifStatement.write(w);
        w.dec();
        if (elseStatement != null) {
            w.line("} else {");
            w.inc();
            elseStatement.write(w);
            w.dec();
        }
        w.line("}");
    }
}
