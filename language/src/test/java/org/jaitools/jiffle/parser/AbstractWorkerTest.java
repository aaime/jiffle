package org.jaitools.jiffle.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hamcrest.Matchers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Base class for worker tests
 * @param <T> The worker type
 */
public abstract class AbstractWorkerTest<T extends BaseWorker> {
    final Logger LOGGER = Logger.getLogger(getClass().getName());

    /**
     * Checks the script has no errors
     *
     * @param scriptFileName
     * @param error
     * @throws Exception
     */
    protected void assertFileHasNoErrors(String scriptFileName) throws Exception {
        Pair<ParseTree, T> result = parseFileAndDoWork(scriptFileName);
        assertThat(result.b.messages, Matchers.hasProperty("error", equalTo(false)));
    }


    /**
     * Checks the script has the expected error type (among others)
     * 
     * @param scriptFileName
     * @param errorMessage
     * @throws Exception
     */
    protected void assertFileHasError(String scriptFileName, String errorMessage) throws Exception {
        Pair<ParseTree, T> result = parseFileAndDoWork(scriptFileName);
        assertWorkerHasError(result.b, errorMessage);
    }

    /**
     * Checks the script has no errors
     *
     * @param scriptFileName
     * @param error
     * @throws Exception
     */
    protected void assertScriptHasNoErrors(String script) throws Exception {
        Pair<ParseTree, T> result = parseStringAndDoWork(script);
        assertThat(result.b.messages, Matchers.hasProperty("error", equalTo(false)));
    }

    /**
     * Checks the script has the expected error type (among others)
     *
     * @param errorMesssage
     * @param scriptFileName
     * @throws Exception
     */
    protected void assertScriptHasError(String script, String errorMesssage) throws Exception {
        Pair<ParseTree, T> result = parseStringAndDoWork(script);
        assertWorkerHasError(result.b, errorMesssage);
    }

    protected Pair<ParseTree, T> parseFileAndDoWork(String scriptFileName) throws Exception {
        InputStream input = getClass().getResourceAsStream(scriptFileName);
        ParseTree tree = ParseHelper.parse(input);
        
        return runWorker(tree);
    }

    protected Pair<ParseTree, T> parseStringAndDoWork(String script) throws Exception {
        ParseTree tree = ParseHelper.parse(script);

        return runWorker(tree);
    }

    protected abstract Pair<ParseTree,T> runWorker(ParseTree tree) throws Exception;

    protected void assertWorkerHasError(BaseWorker worker, String errorMessage) {
        List<String> errors = new ArrayList<>();
        for (Message cm : worker.messages.getMessages()) {
            LOGGER.info(cm.msg);

            if (cm.level == Message.Level.ERROR) {
                errors.add(cm.msg);
            }
        }

        assertThat(errors, hasItems(equalTo(errorMessage)));
    }
}
