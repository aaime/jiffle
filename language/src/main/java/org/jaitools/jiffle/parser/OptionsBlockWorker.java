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

import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jaitools.jiffle.parser.JiffleParser.OptionContext;
import org.jaitools.jiffle.parser.JiffleParser.OptionsBlockContext;

/**
 *
 * @author michael
 */
public class OptionsBlockWorker extends BaseWorker {

    public final Map<String, String> options;
    private boolean readBlock = false;

    public OptionsBlockWorker(ParseTree tree) {
        super(tree);
        options = new HashMap<String, String>();
        walkTree();
    }

    @Override
    public void enterOptionsBlock(OptionsBlockContext ctx) {
        if (readBlock) {
            messages.error(ctx.start, "Script has more than one options block");
        }
    }

    @Override
    public void exitOptionsBlock(OptionsBlockContext ctx) {
        if (!readBlock) {
            for (OptionContext oc : ctx.option()) {
                addOption(oc);
            }
            readBlock = true;
        }
    }

    private void addOption(OptionContext oc) {
        String name = oc.ID().getText();
        String value = oc.optionValue().getText();
        try {
            
            if (OptionLookup.isValidValue(name, value)) {
                if ("null".equals(value)) {
                    value = "Double.NaN";
                } else if (ConstantLookup.isDefined(value)) {
                    double constantValue = ConstantLookup.getValue(value);
                    if (Double.isNaN(constantValue)) {
                        value = "Double.NaN";
                    } else {
                        value = Double.toString(constantValue);
                    }
                    
                }
                options.put(name, value);
            } else {
                messages.error(oc.getStart(),
                        String.format("Invalid value (%s) for option %s",
                        value, name));
            }
        } catch (UndefinedOptionException ex) {
            messages.error(oc.getStart(), "Unknown option " + name);
        }
    }
}
