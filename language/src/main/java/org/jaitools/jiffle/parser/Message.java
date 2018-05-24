package org.jaitools.jiffle.parser;

import java.util.Objects;

/**
 * A message for an error, warning or general info.
 * 
 * @author michael
 */
public class Message {
    public enum Level {
        ERROR,
        WARNING,
        INFO;
    }
    
    protected final Level level;
    protected final String msg;

    public Message(Level level, String msg) {
        this.level = level;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", level, msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return level == message.level &&
                Objects.equals(msg, message.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, msg);
    }
}
