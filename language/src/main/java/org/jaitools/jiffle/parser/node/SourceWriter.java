package org.jaitools.jiffle.parser.node;

import java.util.Arrays;

/**
 * Helper class for writing source code, handles proper indentation
 */
public class SourceWriter {

    private StringBuilder sb = new StringBuilder();
    private int indentStep = 4;
    private int indentAmount = 0;
    private String indentation = "";

    /**
     * Writes out a node to a SourceWriter and returns the resulting script
     * 
     * @param node
     * @return
     */
    public static String write(Expression node) {
        SourceWriter sw = new SourceWriter();
        node.write(sw);
        return sw.getSource();
    }

    /**
     * Increases indentation by one indentation step
     */
    public void inc() {
        indentAmount += indentStep;
    }

    /**
     * Decreases indentation by one indentation step, or reduce indentation to zero otherwise
     */
    public void dec() {
        if (indentStep < indentAmount) {
            indentAmount -= indentStep;
        } else {
            indentAmount = 0;
        }
    }

    /**
     * Method to add a line in the source code.
     * Writes the indentation, the line provided, and adds a newline at the end 
     * @param line
     */
    public void line(String line) {
        String indentation = getIndentation();
        sb.append(indentation).append(line).append("\n");
    }

    /**
     * Method to add text in the source, without any indentation or newline 
     * @param line
     */
    public void append(String text) {
        sb.append(text);
    }

    /**
     * Returns the source code built so far
     * @return
     */
    public String getSource() {
        return sb.toString();
    }

    private String getIndentation() {
        if (indentation.length() < indentAmount) {
            char[] charArray = new char[indentAmount];
            Arrays.fill(charArray, ' ');
            indentation = new String(charArray);
        } else if (indentation.length() > indentAmount) {
            indentation = indentation.substring(0, indentAmount);
        }
        return indentation;
    }

    public void line() {
        sb.append("\n");
    }

    public SourceWriter indent() {
        sb.append(getIndentation());
        return this;
    }

    public SourceWriter append(Node node) {
        node.write(this);
        return this;
        
    }
}
