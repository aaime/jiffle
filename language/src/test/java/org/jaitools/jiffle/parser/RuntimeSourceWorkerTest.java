package org.jaitools.jiffle.parser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.Jiffle.RuntimeModel;
import org.jaitools.jiffle.parser.node.*;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author michael
 */
public class RuntimeSourceWorkerTest {
    
    static final boolean INTERACTIVE = Boolean.getBoolean("interactive");
    
    @Test
    public void foo() throws Exception {
        assertGeneratedSource("mandelbrot.jfl", RuntimeModel.DIRECT, "result");
        assertGeneratedSource("mandelbrot.jfl", RuntimeModel.INDIRECT, "result");
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
        
        VarWorker vw = new VarWorker(tree, ibw.imageVars);
        ExpressionWorker ew = new ExpressionWorker(tree, vw);
        
        RuntimeSourceWorker rsw = new RuntimeSourceWorker(tree, ew.getProperties(), ew.getScopes(), model);
        
        Script script = rsw.getScriptNode();
        SourceWriter writer = new SourceWriter();
        script.write(writer, model);

        String referenceName = FilenameUtils.getBaseName(scriptFileName) + "-" + model + ".java";
        SourceAssert.compare(new File("./src/test/resources/reference", referenceName), writer.getSource());
    }
    
}
