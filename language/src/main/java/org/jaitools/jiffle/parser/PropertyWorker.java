/*
 * Copyright (c) 2018, Michael Bedward. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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