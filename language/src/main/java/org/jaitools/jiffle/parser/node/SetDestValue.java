package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.Jiffle.RuntimeModel;
import org.jaitools.jiffle.parser.*;


/**
 *
 * @author michael
 */
public class SetDestValue extends Expression {
    private final String destVar;
    private final Expression expr;
    
    private static JiffleType ensureScalar(Expression e) throws NodeException {
        JiffleType type = e.getType();
        if (type == JiffleType.D) {
            return type;
        }
        throw new NodeException(Errors.EXPECTED_SCALAR);
    }

    public SetDestValue(String varName, Expression expr) 
            throws NodeException {
        
        super(ensureScalar(expr));
        
        this.destVar = varName;
        this.expr = expr;
    }
    
    @Override
    public String toString() {
        return DirectSources.setDestValue(RuntimeModel.DIRECT, destVar, expr.toString());
    }

    public void write(SourceWriter w) {
        RuntimeModel runtimeModel = w.getRuntimeModel();
        switch (runtimeModel) {
            case DIRECT:
                if (w.isInternalBaseClass()) {
                    w.append("d_").append(destVar).append(".write(_x, _y, 0, ").append(expr).append(")");                    
                } else {
                    w.append("writeToImage(\"").append(destVar).append("\", _x, _y, 0, ").append(expr).append(")");
                }
                
                break;
                
            case INDIRECT:
                w.append("result = ").append(expr);
                break;

            default:
                throw new IllegalArgumentException("Invalid runtime model: " + runtimeModel);
        }
    }
}
