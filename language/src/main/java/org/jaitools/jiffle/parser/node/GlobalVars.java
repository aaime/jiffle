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

import java.util.Collections;
import java.util.List;

/**
 *
 * @author michael
 */
public class GlobalVars extends AbstractNode {
    private final List<BinaryExpression> inits;

    /**
     * Creates an empty instance.
     */
    public GlobalVars() {
        inits = Collections.emptyList();
    }

    /**
     * Creates an instance containing the given variables and optional 
     * initial values.
     */
    public GlobalVars(List<BinaryExpression> inits) {
        this.inits = inits;
    }

    @Override
    public void write(SourceWriter writer) {
        for (BinaryExpression init : inits) {
            init.writeDefaultValue(writer);
        }
    }

    public void writeFields(SourceWriter w) {
        for (BinaryExpression init : inits) {
            init.writeDeclaration(w);
        }
    }

    public void listNames(SourceWriter w) {
        for(int i = 0; i < inits.size(); i++) {
            w.append("\"");
            inits.get(i).appendName(w);
            w.append("\"");
            if (i < inits.size() - 1) {
                w.append(", ");
            }
        }
    }
}
