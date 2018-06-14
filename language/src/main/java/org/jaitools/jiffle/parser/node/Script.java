package org.jaitools.jiffle.parser.node;

import static java.lang.String.format;

import org.jaitools.jiffle.Jiffle;

/**
 *
 * @author michael
 */
public class Script extends AbstractNode {
    private final StatementList stmts;
    private final GlobalVars globals;

    public Script(GlobalVars globals, StatementList stmts) {
        this.globals = globals;
        this.stmts = stmts;
    }

    public void write(SourceWriter w) {
        // class header
        String packageName = "org.jaitools.jiffle.runtime";
        w.line("package " + packageName + ";");
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
        if (model == Jiffle.RuntimeModel.DIRECT) {
            w.line("if (!isWorldSet()) {");
            w.inc();
            w.line("setDefaultBounds();");
            w.dec();
            w.line("}");
        }
        w.line("if (!_imageScopeVarsInitialized) {");
        w.inc();
        w.line("initImageScopeVars();");
        w.dec();
        w.line("}");
        w.line("_stk.clear();");
        // the actual script
        w.newLine();
        stmts.write(w);
        // closing eval
        w.dec();
        w.line("}");
        // closing class
        w.dec();
        w.line("}");
    }

    
    
}
