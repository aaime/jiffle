package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.janino.SimpleCompiler;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.Jiffle.RuntimeModel;
import org.jaitools.jiffle.parser.node.Script;
import org.jaitools.jiffle.parser.node.SourceWriter;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 *
 * @author michael
 */
public class RuntimeModelWorkerTest {
    
    @Test
    public void mandelbrot() throws Exception {
        assertGeneratedSource("mandelbrot.jfl", RuntimeModel.DIRECT, "result");
        assertGeneratedSource("mandelbrot.jfl", RuntimeModel.INDIRECT, "result");
    }

    @Test
    public void interference() throws Exception {
        assertGeneratedSource("interference.jfl", RuntimeModel.DIRECT, "result");
    }

    @Test
    public void ripple() throws Exception {
        assertGeneratedSource("ripple.jfl", RuntimeModel.DIRECT, "result");
    }

    @Test
    public void squircle() throws Exception {
        assertGeneratedSource("squircle.jfl", RuntimeModel.DIRECT, "result");
    }

    @Test
    public void chessboard() throws Exception {
        assertGeneratedSource("chessboard.jfl", RuntimeModel.DIRECT, "result");
    }


    private void assertGeneratedSource(String scriptFileName, RuntimeModel model) throws Exception {
        assertGeneratedSource(scriptFileName, model, null);
    }
    
    private void assertGeneratedSource(String scriptFileName, RuntimeModel model, String outputName, String... inputNames) throws Exception {
        InputStream input = getClass().getResourceAsStream(scriptFileName);
        ParseTree tree = ParseHelper.parse(input);
        
        ImagesBlockWorker ibw = new ImagesBlockWorker(tree);
        
        // set input and outputs if missing from script
        if (outputName != null) {
            ibw.imageVars.put(outputName, Jiffle.ImageRole.DEST);
        }
        if (inputNames != null) {
            for (String inputName : inputNames) {
                ibw.imageVars.put(inputName, Jiffle.ImageRole.SOURCE);
            }
        }
        
        OptionsBlockWorker ow = new OptionsBlockWorker(tree);
        VarWorker vw = new VarWorker(tree, ibw.imageVars);
        ExpressionWorker ew = new ExpressionWorker(tree, vw);
        
        RuntimeModelWorker rsw = new RuntimeModelWorker(tree, ow.options, ew.getProperties(), ew.getScopes());
        
        Script script = rsw.getScriptNode();
        SourceWriter writer = new SourceWriter(model);
        script.write(writer);

        String referenceName = FilenameUtils.getBaseName(scriptFileName) + "-" + model + ".java";
        String generatedSource = writer.getSource();
        SourceAssert.compare(new File("./src/test/resources/reference", referenceName),
                generatedSource);
        
        // make sure it compiles too
        SimpleCompiler compiler = new SimpleCompiler();
        compiler.cook(generatedSource);

    }
    
}
