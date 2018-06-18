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

package org.jaitools.jiffle.util;

/**
 * A few assorted string operations.
 *
 * @author michael
 */
public class Strings {
    
    /**
     * Calls toString on each object of args and concatenates the
     * results with space delimiters.
     */
    public static String spaces(Object ...args) {
        return concat(' ', args);
    }
    
    public static String commas(Object ...args) {
        return concat(',', args);
    }

    /**
     * Calls toString on each object of args and concatenates the
     * results delimited by sep.
     */
    public static String concat(char sep, Object[] args) {
        int n = args.length;
        if (n == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Object o : args) {
            sb.append(o);
            if (n-- > 1) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }
    
    /**
     * Replacement for String.split that doesn't return
     * empty tokens.
     */
    public static String[] split(String s, String regex) {
        return s.replaceFirst("^" + regex, "").split(regex);
    }
    
}