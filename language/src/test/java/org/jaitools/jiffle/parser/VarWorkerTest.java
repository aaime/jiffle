package org.jaitools.jiffle.parser;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

/**
 *
 * @author michael
 */
public class VarWorkerTest extends AbstractWorkerTest<VarWorker> {

    @Test
    public void scriptNodeIsAnnotatedWithGlobalScope() throws Exception {
        Pair<ParseTree, VarWorker> result = parseFileAndDoWork("ValidScript.jfl");

        ParseTree tree = result.a;
        VarWorker worker = result.b;

        SymbolScope scope = worker.getProperties().get(tree);
        assertThat(scope, instanceOf(GlobalScope.class));
    }

    @Test
    public void imageVarsInGlobalScope() throws Exception {
        Pair<ParseTree, VarWorker> result = parseFileAndDoWork("ValidScript.jfl");

        ParseTree tree = result.a;
        VarWorker worker = result.b;
        assertFalse(worker.messages.isError());

        SymbolScope scope = worker.getProperties().get(tree);

        String[] names = { "src", "dest" };
        Symbol.Type[] types = { Symbol.Type.SOURCE_IMAGE, Symbol.Type.DEST_IMAGE };

        int i = 0;
        for (String name : names) {
            assertTrue( scope.has(name) );
            assertTrue( scope.get(name).getType() == types[i++] );
        }
    }

    @Test
    public void initBlockVarsInGlobalScope() throws Exception {
        Pair<ParseTree, VarWorker> result = parseFileAndDoWork("InitBlockFooBar.jfl");

        ParseTree tree = result.a;
        VarWorker worker = result.b;
        assertFalse(worker.messages.isError());

        SymbolScope scope = worker.getProperties().get(tree);

        assertTrue( scope.has("foo") );
        assertTrue( scope.has("bar") );
    }

    @Test
    public void initBlockWithDuplicateVar() throws Exception {
        assertFileHasError(
                "InitBlockDuplicateVar.jfl",
                Errors.DUPLICATE_VAR_DECL + ": foo");
    }

    @Test
    public void initBlockWithImageVarOnLHS() throws Exception {
        assertFileHasError(
                "InitBlockImageVarLHS.jfl",
                Errors.IMAGE_VAR_INIT_BLOCK + ": src");
    }

    @Test
    public void readingFromDestinationImage() throws Exception {
        assertFileHasError(
                "ReadingFromDestImage.jfl",
                Errors.READING_FROM_DEST_IMAGE + ": dest");
    }

    @Test
    public void writingToSourceImage() throws Exception {
        assertFileHasError(
                "WritingToSourceImage.jfl",
                Errors.WRITING_TO_SOURCE_IMAGE + ": src");
    }

    @Test
    public void assignmentToConstant() throws Exception {
        assertFileHasError(
                "AssignmentToConstant.jfl",
                Errors.ASSIGNMENT_TO_CONSTANT + ": M_PI");
    }

    @Test
    public void assignmentToLoopVar() throws Exception {
        assertFileHasError(
                "AssignmentToLoopVar.jfl",
                Errors.ASSIGNMENT_TO_LOOP_VAR + ": i");
    }

    @Test
    public void invalidAssignmentOpForDestinationImage() throws Exception {
        assertFileHasError(
                "InvalidAssignmentOpForDestinationImage.jfl",
                Errors.INVALID_ASSIGNMENT_OP_WITH_DEST_IMAGE + ": dest");
    }

    @Test
    public void undefinedVariable() throws Exception {
        assertFileHasError(
                "UndefinedVariable.jfl",
                "Variable not initialized prior to use: b");
    }

    protected Pair<ParseTree, VarWorker> runWorker(ParseTree tree) throws Exception {
        ImagesBlockWorker ib = new ImagesBlockWorker(tree);
        return new Pair(tree, new VarWorker(tree, ib.imageVars));
    }

}
