package org.jaitools.jiffle.parser.node;

import javax.swing.*;

public class Until implements Statement {

    private final Expression condition;
    private final StatementList stmts;

    public Until(Expression condition, StatementList stmts) {
        this.condition = condition;
        this.stmts = stmts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("while(_FN.sign(").append(condition).append(") != 1) {\n").append(stmts).append("}");
        return sb.toString();
    }

    public void write(SourceWriter w) {
        w.indent().append("while(");
        condition.write(w);
        w.append(" == 0) {\n");
        w.inc();
        stmts.write(w);
        w.dec();
        w.line("}");
    }
}
