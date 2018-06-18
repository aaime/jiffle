/*
 * Copyright (c) 2018, Michael Bedward. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
