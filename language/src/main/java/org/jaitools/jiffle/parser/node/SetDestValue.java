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
