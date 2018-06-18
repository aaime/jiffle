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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaitools.jiffle.parser;

/**
 * Constants for function and expression types.
 * Each is associated with a label used in the function properties file
 * (META-INF/org/jaitools/jiffle/FunctionLookup.properties).
 * 
 * @author michael
 */
public enum JiffleType {
    /** Scalar double */
    D("D"),
    
    /** List */
    LIST("LIST"),
    
    /** Not known (a placeholder type for the compiler). */
    UNKNOWN("Unknown");
    
    private final String label;
    
    private JiffleType(String label) {
        this.label = label;
    }
    
    /**
     * Gets the type with the given label (case-insensitive).
     * 
     * @param label type label
     * @return the matching type
     * @throws JiffleTypeException if no match exists
     */
    public static JiffleType get(String label) throws JiffleTypeException {
        String s = label.trim().toUpperCase();
        for (JiffleType rt : JiffleType.values()) {
            if (rt.label.equals(s)) {
                return rt;
            }
        }
        
        throw new JiffleTypeException(label);
    }

}
