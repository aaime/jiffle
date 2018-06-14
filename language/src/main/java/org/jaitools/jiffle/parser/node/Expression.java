package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public abstract class Expression implements Node {
    
    protected final JiffleType type;

    protected Expression(JiffleType type) {
        this.type = type;
    }
    
    public JiffleType getType() {
        return type;
    }

    public Expression forceDouble() {
        // inside expressions all literals must be doubles
        if (this instanceof IntLiteral) {
            return new DoubleLiteral(((IntLiteral) this).value);
        }
        return this;
    }

}
