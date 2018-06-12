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

    @Override
    public void write(SourceWriter writer) {
        this.write(writer, Jiffle.RuntimeModel.DIRECT);
    }

    public void write(SourceWriter w, Jiffle.RuntimeModel model) {
        // class header
        String packageName = "org.jaitools.jiffle.runtime";
        w.line("package " + packageName + ";");
        w.line();
        String template = "public class %s extends " + packageName + ".%s {"; 
        if (model == Jiffle.RuntimeModel.DIRECT) {
            w.line(format(template, "JiffleDirectRuntimeImpl", "AbstractDirectRuntime"));
        } else {
            w.line(format(template, "JiffleIndirectRuntimeImpl", "AbstractIndirectRuntime")); 
        }
        w.inc();
        
        globals.writeFields(w);
        w.line();
        w.line("protected void initOptionVars() {");
        w.inc();
        globals.write(w);
        w.dec();
        w.line("}");
        
        w.line();
        
        w.line("public double evaluate(double _x, double _y) {");
        w.inc();
        stmts.write(w);
        w.dec();
        w.line("}");
        
        w.dec();
        w.line("}");
    }

    
    
}
