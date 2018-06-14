package org.jaitools.jiffle.parser.node;

public class LoopInRange implements Statement {

    private Statement statement;
    private Variable loopVariable;
    private Expression low;
    private Expression high;

    public LoopInRange(Variable loopVariable, Expression low, Expression high, Statement statement) {
        this.loopVariable = loopVariable;
        this.low = low;
        this.high = high;
        this.statement = statement;
    }

    @Override
    public void write(SourceWriter w) {
        String lowVariable = "_lo" + loopVariable;
        String highVariable = "_hi" + lowVariable;
        w.indent().append("final int ").append(lowVariable).append(" = (int) (").append(low).append(");").newLine();
        w.indent().append("final int ").append(highVariable).append(" = (int) (").append(high).append(");").newLine();
        w.indent().append("for(int ").append(loopVariable).append(" = ").append(lowVariable);
        w.append("; ").append(loopVariable).append(" <= ").append(highVariable);
        w.append("; ").append(loopVariable).append("++) {").newLine();
        w.inc();
        statement.write(w);
        w.dec();
        w.line("}");
    }
}
