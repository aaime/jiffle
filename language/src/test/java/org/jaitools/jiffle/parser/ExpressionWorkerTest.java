package org.jaitools.jiffle.parser;

import static org.junit.Assert.assertFalse;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class ExpressionWorkerTest extends AbstractWorkerTest<ExpressionWorker> {

    @Test
    public void testValid() throws Exception {
        Pair<ParseTree, ExpressionWorker> pair = parseFileAndDoWork("ValidScript.jfl");
        ExpressionWorker worker = pair.b;
        assertFalse(worker.messages.isError());
    }

    @Test
    public void testAssignListToScalar() throws Exception {
        assertFileHasError("AssignListToScalar.jfl", Errors.ASSIGNMENT_LIST_TO_SCALAR.toString());
    }

    @Test
    public void testAssignScalarToList() throws Exception {
        assertFileHasError("AssignScalarToList.jfl", Errors.ASSIGNMENT_SCALAR_TO_LIST.toString());
    }

    @Test
    public void testCombinedAssignToList() throws Exception {
        String template = "list = [1, 2, 3];\n" +
                "list <op> 3;";
        // invalid
        String[] operators = new String[] {"+=", "-=", "*=", "/=", "%="};
        for (String op : operators) {
            String script = template.replace("<op>", op);
            LOGGER.info("Testing operator " + op);
            assertScriptHasError(script, Errors.INVALID_OPERATION_FOR_LIST.toString());    
        }
        // valid
        assertScriptHasNoErrors(template.replace("<op>", "="));
    }

    @Test
    public void testListInTernary() throws Exception {
        assertFileHasError("ListInTernary.jfl", Errors.LIST_AS_TERNARY_CONDITION.toString());
    }

    @Test
    public void testListInTernaryComparison() throws Exception {
        assertFileHasError("ListInTernaryComparison.jfl",
                Errors.LIST_AS_TERNARY_CONDITION.toString());
    }

    @Test
    public void testScalarInTernary() throws Exception {
        assertFileHasNoErrors("ScalarInTernary.jfl");
    }

    @Test
    public void testUninitializedVariable() throws Exception {
        assertFileHasError("UninitializedVariable.jfl", Errors.UNINIT_VAR + ": b");
    }
    
    @Override
    protected Pair<ParseTree, ExpressionWorker> runWorker(ParseTree tree)
            throws Exception {
        ImagesBlockWorker ibw = new ImagesBlockWorker(tree);
        VarWorker vw = new VarWorker(tree, ibw.imageVars);
        ExpressionWorker ew = new ExpressionWorker(tree, vw);
        return new Pair(tree, ew);
    }
}
