package org.jaitools.jiffle.parser.node;

import static java.lang.String.format;

import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.parser.JiffleParserException;
import org.jaitools.jiffle.parser.OptionLookup;
import org.jaitools.jiffle.parser.UndefinedOptionException;
import org.jaitools.jiffle.runtime.JiffleRuntimeException;

import java.util.Map;

/**
 *
 * @author michael
 */
public class Script extends AbstractNode {
    private final StatementList stmts;
    private Map<String, String> options;
    private final GlobalVars globals;

    public Script(Map<String, String> options, GlobalVars globals,
            StatementList stmts) {
        this.options = options;
        this.globals = globals;
        this.stmts = stmts;
    }

    public void write(SourceWriter w) {
        // class header
        String packageName = "org.jaitools.jiffle.runtime";
        w.line("package " + packageName + ";");
        w.newLine();
        w.line("import java.util.List;");
        w.line("import java.util.ArrayList;");
        w.line("import java.util.Arrays;");
        w.newLine();

        // add the script source, if available
        String script = w.getScript();
        if (script != null) {
            String[] lines = script.split("\n");
            w.line("/**");
            w.line(" * Java runtime class generated from the following Jiffle script: ");
            w.line(" *<code>");
            for (String line : lines) {
                w.append(" * ").append(line).newLine();
            }
            w.line(" *</code>");
            w.line(" */");
        }

        // class declaration
        String template = "public class %s extends %s {";
        String className;
        Jiffle.RuntimeModel model = w.getRuntimeModel();
        if (model == Jiffle.RuntimeModel.DIRECT) {
            className = "JiffleDirectRuntimeImpl";
        } else {
            className = "JiffleIndirectRuntimeImpl"; 
        }
        w.line(format(template, className, w.getBaseClassName()));
        
        // writing class fields
        w.inc();
        globals.writeFields(w);
        w.newLine();
        
        // adding the constructor
        w.indent().append("public ").append(className).append("() {").newLine();
        w.inc();
        w.indent().append("super(new String[] {");
        globals.listNames(w);
        w.append("});").newLine();
        w.dec();
        w.line("}");
        w.newLine();
        
        // add the options init, if required
        if (options != null && !options.isEmpty()) {
            w.line("protected void initOptionVars() {");
            w.inc();
            for (Map.Entry<String, String> entry : options.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                try {
                    String activeExpr = OptionLookup.getActiveRuntimExpr(name, value);
                    w.line(activeExpr);
                } catch (UndefinedOptionException e) {
                    throw new JiffleParserException(e);
                }
            }
            w.dec();
            w.line("}");
        }
        
        // and field initializer method
        w.line("protected void initImageScopeVars() {");
        w.inc();
        globals.write(w);
        w.line("_imageScopeVarsInitialized = true;");
        w.dec();
        w.line("}");
        w.newLine();
        
        // the evaluate method
        if (model == Jiffle.RuntimeModel.DIRECT) {
            w.line("public void evaluate(double _x, double _y) {");
        } else {
            w.line("public double evaluate(double _x, double _y) {");
        }
        w.inc();
        
        // basic checks at the beginnig of pixel evaluation
        w.line("if (!isWorldSet()) {");
        w.inc();
        w.line("setDefaultBounds();");
        w.dec();
        w.line("}");
        w.line("if (!_imageScopeVarsInitialized) {");
        w.inc();
        w.line("initImageScopeVars();");
        w.dec();
        w.line("}");
        w.line("_stk.clear();");
        if (model == Jiffle.RuntimeModel.INDIRECT) {
            w.line("double result = Double.NaN;");
        }

        // the actual script
        w.newLine();
        stmts.write(w);
        
        // in case of indirect runtime, return the result at the end
        if (model == Jiffle.RuntimeModel.INDIRECT) {
            w.line("return result;");
        }
        
        w.dec();
        w.line("}");
        
        // closing class
        w.dec();
        w.line("}");
    }

    
    
}
