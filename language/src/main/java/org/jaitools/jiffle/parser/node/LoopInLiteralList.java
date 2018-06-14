package org.jaitools.jiffle.parser.node;

public class LoopInLiteralList implements Statement {

    private Statement statement;
    private Variable loopVariable;
    private ListLiteral listLiteral;

    public LoopInLiteralList(Variable loopVariable, ListLiteral listLiteral, Statement statement) {
        this.loopVariable = loopVariable;
        this.listLiteral = listLiteral;
        this.statement = statement;
    }

    @Override
    public void write(SourceWriter w) {
        w.indent().append("for(Double ").append(loopVariable).append(" : ").append(listLiteral);
        w.append(") {").newLine();
        w.inc();
        statement.write(w);
        w.dec();
        w.line("}");
    }
}
