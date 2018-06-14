package org.jaitools.jiffle.parser.node;

public class LoopInVariable implements Statement {

    private Statement statement;
    private Variable loopVariable;
    private Variable listVariable;

    public LoopInVariable(Variable loopVariable, Variable listVariable, Statement statement) {
        this.loopVariable = loopVariable;
        this.listVariable = listVariable;
        this.statement = statement;
    }

    @Override
    public void write(SourceWriter w) {
        w.indent().append("for(Double ").append(loopVariable).append(" : ").append(listVariable);
        w.append(") {").newLine();
        w.inc();
        statement.write(w);
        w.dec();
        w.line("}");
    }
}
