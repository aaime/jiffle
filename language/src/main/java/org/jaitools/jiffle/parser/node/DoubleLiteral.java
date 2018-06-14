package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class DoubleLiteral extends ScalarLiteral {
    
    private static String checkValue(String value) {
        // will throw a NumberFormatException if not a valid double
        return Double.valueOf(value).toString();
    }

    public DoubleLiteral(String value) {
        super(checkValue(value));
    }

}
