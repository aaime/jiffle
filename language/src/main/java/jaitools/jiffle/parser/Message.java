/*
 * Copyright 2009-2011 Michael Bedward
 * 
 * This file is part of jai-tools.
 *
 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package jaitools.jiffle.parser;

/**
 * Constants used by the Jiffle script and tree parsers to report errors 
 * and warnings.
 * 
 * @see Level
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public enum Message {
    
    /**
     * Error: Invalid use of an image variable for both input and output.
     */
    ASSIGNMENT_TO_SRC_IMAGE(Level.ERROR, 
            "Cannot assign a value to a non-destination image"),
    
    /**
     * Error: attempting to assign a value to a loop variable.
     */
    ASSIGNMENT_TO_LOOP_VAR(Level.ERROR,
            "Cannot assign a new value to a loop variable"),
    
    /**
     * Error: attempting to assign a list to a scalar variable.
     */
    ASSIGNMENT_LIST_TO_SCALAR(Level.ERROR,
            "Cannot assign a list to a scalar variable"),
    
    /**
     * Error: attempting to assign a scalar to a list variable.
     */
    ASSIGNMENT_SCALAR_TO_LIST(Level.ERROR,
            "Cannot assign a scalar to a list variable"),
    
    /**
     * Error: constant on the left hand side of an assignment.
     */
    CONSTANT_LHS(Level.ERROR,
            "Constant on the left hand side of an expression"),
    
    /**
     * Warning: an image variable parameter was passed to Jiffle but not 
     * used in the script.
     */
    IMAGE_NOT_USED(Level.WARNING,
            "Image variable is defined but not used"),
    
    /**
     * Error: trying to assign a value to an image variable in the init block.
     */
    IMAGE_VAR_INIT_LHS(Level.ERROR,
            "A value cannot be assigned to an image var in the init block"),
    
    /**
     * Error: using an assignment operator other than '=' with a 
     * destination image variable.
     */
    INVALID_ASSIGNMENT_OP_WITH_DEST_IMAGE(Level.ERROR,
            "Invalid assignment op with destination image"),
    
    /**
     * Error: invalid operation for a list variable.
     */
    INVALID_OPERATION_FOR_LIST(Level.ERROR,
            "Invalid operation for list variable"),
    
    /**
     * Warning: script option not recognized.
     */
    INVALID_OPTION(Level.WARNING,
            "Unrecognized option"),
    
    /**
     * Warning: invalid script option value is ignored.
     */
    INVALID_OPTION_VALUE(Level.WARNING,
            "Invalid option value ignored"),
    
    /**
     * Error: Image position syntax cannot be used with a destination image
     * variable.
     */
    IMAGE_POS_ON_DEST(Level.ERROR,
            "Image position cannot be specified for a destination image"),
    
    /**
     * Error: trying to use image position syntax with a non-image variable.
     */
    IMAGE_POS_ON_NON_IMAGE(Level.ERROR,
            "Image position specifier(s) used with a non-image variable"),
    
    /**
     * Error: list arg invalid for this function
     */
    NON_LIST_FUNCTION(Level.ERROR,
            "List argument cannot be used with this function"),
    
    /**
     * Error: trying to read from a destination image.
     */
    READING_FROM_DEST_IMAGE(Level.ERROR, 
            "Cannot read a value from a destination image"),
    
    /**
     * Error: source image variable cannot appear in the init block.
     */
    SRC_IMAGE_IN_INIT_BLOCK(Level.ERROR,
            "Source images cannot be referenced in an init block"),
    
    /**
     * Error: list operation with an undeclared list variable.
     */
    UNDECLARED_LIST_VAR(Level.ERROR,
            "List variable has not been declared"),
    
    /**
     * Error: call to an undefined function.
     */
    UNDEFINED_FUNCTION(Level.ERROR,
            "Call to undefined function"),
    
    /**
     * Error: a non-image variable used before being assigned a value.
     */
    UNINIT_VAR(Level.ERROR, 
            "Variable used before being assigned a value");
    
    private Level level;
    private String desc;

    private Message(Level level, String desc) {
        this.level = level;
        this.desc = desc;
    }
    
    /**
     * Tests if this is an error
     * @return {@code true} if an error, {@code false} otherwise
     */
    public boolean isError() {
        return level == Level.ERROR;
    }
    
    /**
     * Tests if this is a warning.
     * @return {@code true} if a warning, {@code false} otherwise
     */
    public boolean isWarning() {
        return level == Level.WARNING;
    }

    /**
     * Returns a formatted string for the error or warning.
     * @return a string
     */
    @Override
    public String toString() {
        if (isError()) {
            return "Error: " + desc;
        } else {
            return "Warning: " + desc;
        }
    }
}


