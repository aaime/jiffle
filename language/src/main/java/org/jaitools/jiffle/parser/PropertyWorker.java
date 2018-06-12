package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public abstract class PropertyWorker<T> extends BaseWorker {
    final Logger LOGGER = Logger.getLogger(getClass().getName());
    
    protected final TreeNodeProperties<T> properties;
    
    public PropertyWorker(ParseTree tree) {
        super(tree);
        this.properties = new TreeNodeProperties<T>();
    }

    public TreeNodeProperties<T> getProperties() {
        return new TreeNodeProperties<T>(properties);
    }
    
    protected T get(ParseTree ctx) {
        return properties.get(ctx);
    }
    
    protected T getOrElse(ParseTree ctx, T fallback) {
        if (ctx == null) {
            return fallback;
        } 
        T prop = properties.get(ctx);
        return prop == null ? fallback : prop;
    }

    protected void set(ParseTree ctx, T node) {
        if (ctx instanceof ParserRuleContext && LOGGER.isLoggable(Level.FINE)) {
            ParserRuleContext prc = (ParserRuleContext) ctx;
            Token start = prc.getStart();
            String lineColumn = "(" + start.getLine() + ":" + start.getCharPositionInLine() + ")";
            LOGGER.fine(
                    "Token "
                            + start.getText()
                            + ", type "
                            + ctx.getClass().getSimpleName()
                            + " at "
                            + lineColumn
                            + " set to "
                            + node);
        }

        properties.put(ctx, node);
    }
}
