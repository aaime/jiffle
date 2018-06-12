package org.jaitools.jiffle.parser.node;

/**
 *
 * @author michael
 */
public interface Node {

//    /**
//     * Writes out the contents of the node as Java code on the provided SourceWriter
//     * @param w
//     * TODO: remove this default implementation
//     */
//    default void write(SourceWriter w) {
//        String line = toString();
//        w.append(line);
//    }

    void write(SourceWriter w);
    
}
