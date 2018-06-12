package org.jaitools.jiffle.parser;

import java.io.InputStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jaitools.jiffle.Jiffle.RuntimeModel;
import org.jaitools.jiffle.parser.node.*;
import org.junit.Test;

/**
 *
 * @author michael
 */
public class RuntimeSourceWorkerTest {
    
    @Test
    public void foo() throws Exception {
        doParseAndWork("mandelbrot.jfl");
    }
    
    private void doParseAndWork(String scriptFileName) throws Exception {
        InputStream input = getClass().getResourceAsStream(scriptFileName);
        ParseTree tree = ParseHelper.parse(input);
        
        ImagesBlockWorker ibw = new ImagesBlockWorker(tree);
        VarWorker vw = new VarWorker(tree, ibw.imageVars);
        
        ExpressionWorker ew = new ExpressionWorker(tree, vw);
        
        RuntimeSourceWorker rsw = new RuntimeSourceWorker(tree, ew, RuntimeModel.DIRECT);
        
        Script script = rsw.getScriptNode();
        SourceWriter writer = new SourceWriter();
        script.write(writer);
        System.out.println(writer.getSource());
    }
    
}
