package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.Jiffle.RuntimeModel;
import org.jaitools.jiffle.parser.*;


/**
 *
 * @author michael
 */
public class SetDestValue extends Expression {
    private final RuntimeModel runtimeModel;
    private final String destVar;
    private final Expression expr;
    
    private static JiffleType ensureScalar(Expression e) throws NodeException {
        JiffleType type = e.getType();
        if (type == JiffleType.D) {
            return type;
        }
        throw new NodeException(Errors.EXPECTED_SCALAR);
    }

    public SetDestValue(Jiffle.RuntimeModel runtimeModel, String varName, Expression expr) 
            throws NodeException {
        
        super(ensureScalar(expr));
        
        this.runtimeModel = runtimeModel;
        this.destVar = varName;
        this.expr = expr;
    }
    
    @Override
    public String toString() {
        return DirectSources.setDestValue(runtimeModel, destVar, expr.toString());
    }

    public void write(SourceWriter w) {
        switch (runtimeModel) {
            case DIRECT:
                w.append("writeToImage(\"").append(destVar).append("\", _x, _y, 0, ");
                expr.write(w);
                w.append(")");
                break;
                
            case INDIRECT:
                w.append("return ").append(expr);
                break;

            default:
                throw new IllegalArgumentException("Invalid runtime model: " + runtimeModel);
        }
    }
}
