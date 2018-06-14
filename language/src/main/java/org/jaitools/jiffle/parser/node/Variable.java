package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class Variable extends Expression {
    private final String name;

    public Variable(String name, JiffleType d) {
        super(d);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void write(SourceWriter w) {
        w.append("v_").append(name);
    }
    
}
