package org.jaitools.jiffle.parser.node;

public class ListAppend implements Statement {

    private Variable var;
    private Expression expression;

    public ListAppend(Variable var, Expression expression) {
        this.var = var;
        this.expression = expression;
    }

    @Override
    public void write(SourceWriter w) {
        w.indent().append(var).append(".add(").append(expression).append(");").newLine();
    }
}
