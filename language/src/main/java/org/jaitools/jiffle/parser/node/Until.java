package org.jaitools.jiffle.parser.node;

public class Until implements Statement {

    private final Expression condition;
    private final Statement statement;

    public Until(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("while(_FN.sign(").append(condition).append(") != 1) {\n").append(statement).append("}");
        return sb.toString();
    }

    public void write(SourceWriter w) {
        w.indent().append("while (!_FN.isTrue(");
        condition.write(w);
        w.append(")) {\n");
        w.inc();
        statement.write(w);
        w.dec();
        w.line("}");
    }
}
