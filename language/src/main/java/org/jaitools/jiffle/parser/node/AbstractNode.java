package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.Jiffle;

/**
 * Base class for AbstractNode providing a default implementation of toString
 */
public abstract class AbstractNode implements Node {
    
    public String toString() {
        SourceWriter sw = new SourceWriter(Jiffle.RuntimeModel.DIRECT);
        write(sw);
        return sw.toString();
    }

    @Override
    public void write(SourceWriter w) {
        throw new UnsupportedOperationException("Write should be implemented!");
    }
}
