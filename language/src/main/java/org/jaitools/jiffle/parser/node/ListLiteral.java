package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.JiffleType;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author michael
 */
public class ListLiteral extends Expression {
    private final List<Expression> args;
    
    public ListLiteral(List<Expression> args) {
        super(JiffleType.LIST);
        this.args = new ArrayList<>();
        for (Expression arg : args) {
            this.args.add(arg.forceDouble());
        }
    }

    public void write(SourceWriter w) {
        if (args.isEmpty()) {
            w.append("new ArrayList()");
        } else {
            w.append("new ArrayList(Arrays.asList(");
            for (int i = 0; i < args.size(); i++) {
                Expression arg = args.get(i);
                w.append(arg);
                if (i < args.size() - 1) {
                    w.append(", ");
                }
            }
            w.append("))"); 
        }
    }
}
