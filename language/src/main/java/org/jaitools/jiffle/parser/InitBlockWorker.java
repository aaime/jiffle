package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashMap;
import java.util.Map;

public class InitBlockWorker extends BaseWorker {

    public final Map<String, String> variables;
    private boolean readBlock = false;

    public InitBlockWorker(ParseTree tree) {
        super(tree);
        variables = new HashMap<>();
        walkTree();
    }

    @Override
    public void enterInitBlock(JiffleParser.InitBlockContext ctx) {
        if (readBlock) {
            messages.error(ctx.start, "Script has more than one init block");
        }
    }

    @Override
    public void exitInitBlock(JiffleParser.InitBlockContext ctx) {
        if (!readBlock) {
            for (JiffleParser.VarDeclarationContext vd : ctx.varDeclaration()) {
                addVariable(vd);
            }
            readBlock = true;
        }
    }

    private void addVariable(JiffleParser.VarDeclarationContext vd) {
        String name = vd.ID().getText();
        // String value = vd.expression();
        variables.put(name, null);
    }
}
