package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.Errors;
import org.jaitools.jiffle.parser.JiffleParser;
import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class BinaryExpression extends Expression {

    public static enum Operator {

        PLUS(JiffleParser.PLUS, "%s + %s"),
        MINUS(JiffleParser.MINUS, "%s - %s"),
        TIMES(JiffleParser.TIMES, "%s * %s"),
        DIV(JiffleParser.DIV, "%s / %s"),
        MOD(JiffleParser.MOD, "%s %% %s"),
        
        POW(JiffleParser.POW, "Math.pow(%s, %s)"),
        
        ASSIGN(JiffleParser.ASSIGN, "%s = %s"),
        PLUSEQ(JiffleParser.PLUSEQ, "%s += %s"),
        MINUSEQ(JiffleParser.MINUSEQ, "%s -= %s"),
        TIMESEQ(JiffleParser.TIMESEQ, "%s *= %s"),
        DIVEQ(JiffleParser.DIVEQ, "%s /= %s"),
        
        UNKNOWN(-1, "");
        
        private final int code;
        private final String fmt;

        private Operator(int code, String fmt) {
            this.code = code;
            this.fmt = fmt;
        }
        
        public static Operator get(int code) {
            for (Operator o : Operator.values()) {
                if (code == o.code) {
                    return o;
                }
            }
            
            return UNKNOWN;
        }
        
        public String getFormat() {
            return fmt;
        }
    }

    private final boolean declarationNeeded;
    private final Expression left;
    private final Expression right;
    private final Operator op;
    

    private static JiffleType getReturnType(Expression left, Expression right) {
        if (left.getType() == right.getType()) {
            return left.getType();
        } else {
            // types are different so result must be list
            return JiffleType.LIST;
        }
    }

    public BinaryExpression(int opCode, Expression left, Expression right)
            throws NodeException {
        this(opCode, left, right, false);
    }

    public BinaryExpression(int opCode, Expression left, Expression right, boolean declarationNeeded) 
            throws NodeException {
        
        super(getReturnType(left, right));
        
        this.op = Operator.get(opCode);
        if (op == Operator.UNKNOWN) {
            throw new NodeException(Errors.INVALID_BINARY_EXPRESSION);
        }

        this.left = left.forceDouble();
        this.right = right.forceDouble();
        this.declarationNeeded = declarationNeeded;
    }

    public void write(SourceWriter w) {
        if (declarationNeeded && left instanceof Variable) {
            String type = getJavaType();
            w.append(type).append(" ");
        }
        String leftCode = w.writeToString(left);
        String rightCode = w.writeToString(right);
        w.append(String.format(op.getFormat(), leftCode, rightCode));
    }

    private String getJavaType() {
        return left.getType() == JiffleType.D ? "double" : "List";
    }

    private String getInitialValue() {
        return left.getType() == JiffleType.D ? "Double.NaN" : "null";
    }

    public void writeDeclaration(SourceWriter w) {
        if (left instanceof Variable) {
            String type = getJavaType();
            String initialValue = getInitialValue();
            w.indent()
                    .append(type)
                    .append(" ")
                    .append(left)
                    .append(" = ")
                    .append(initialValue)
                    .append(";")
                    .newLine();
        }
    }

    public void appendName(SourceWriter w) {
        if (left instanceof Variable) {
            w.append(left.toString());
        }
    }

    public void writeDefaultValue(SourceWriter w) {
        if (!(left instanceof Variable)) {
            throw new IllegalStateException("Cannot write default value unless this is a declaration");
        }
        w.indent().append("if (Double.isNaN(").append(left).append(")) {").newLine();
        w.inc();
        w.indent().append(this).append(";").newLine();
        w.dec();
        w.line("}");
    }

}
