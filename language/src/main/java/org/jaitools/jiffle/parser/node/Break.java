package org.jaitools.jiffle.parser.node;

public class Break implements Statement {
    @Override
    public void write(SourceWriter w) {
        w.line("break;");
    }
}
