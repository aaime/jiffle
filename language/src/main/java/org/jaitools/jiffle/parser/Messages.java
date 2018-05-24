package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds a collection of messages and provides short-cut methods to create them.
 *
 * @author michael
 */
public class Messages {
    private List<Message> messages = new ArrayList<>();
    private boolean error;
    private boolean warning;

    public void error(Token tok, String msg) {
        messages.add(new CompilerMessage(CompilerMessage.Level.ERROR, tok, msg));
        error = true;
    }

    public void error(Token tok, Errors error) {
        messages.add(new CompilerMessage(CompilerMessage.Level.ERROR, tok, error.toString()));
    }

    public void error(int line, int charPos, String msg) {
        messages.add(new CompilerMessage(CompilerMessage.Level.ERROR, line, charPos, msg));
        error = true;
    }

    public void warning(Token tok, String msg) {
        messages.add(new CompilerMessage(CompilerMessage.Level.WARNING, tok, msg));
        warning = true;
    }

    public void warning(int line, int charPos, String msg) {
        messages.add(new CompilerMessage(CompilerMessage.Level.WARNING, line, charPos, msg));
        warning = true;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public boolean isError() {
        return error;
    }

    public boolean isWarning() {
        return warning;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Message msg : messages) {
            sb.append(msg.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
