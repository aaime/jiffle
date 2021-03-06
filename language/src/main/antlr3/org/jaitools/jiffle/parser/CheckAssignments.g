/*
 * Copyright 2011 Michael Bedward
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

/**
 * Checks for valid use of image variables.
 *
 * @author Michael Bedward
 */

tree grammar CheckAssignments;

options {
    ASTLabelType = CommonTree;
    tokenVocab = Jiffle;
}

@header {
package org.jaitools.jiffle.parser;

import java.util.Set;
import org.jaitools.CollectionFactory;
}

@members {

private MessageTable msgTable;
private SymbolScopeStack varScope;

public CheckAssignments(TreeNodeStream input, MessageTable msgTable) {
    this(input);
    if (msgTable == null) {
        throw new IllegalArgumentException( "msgTable should not be null" );
    }
    this.msgTable = msgTable;

    varScope = new SymbolScopeStack();
}

}


start
@init {
    varScope.addLevel("top");
}               : jiffleOption* varDeclaration* statement+
                ;


jiffleOption    : ^(JIFFLE_OPTION ID .)
                ;


varDeclaration  : ^(DECL VAR_DEST ID)
                | ^(DECL VAR_SOURCE ID)

                | ^(DECL VAR_IMAGE_SCOPE .?)
                { 
                    varScope.addSymbol($VAR_IMAGE_SCOPE.text, SymbolType.SCALAR, ScopeType.IMAGE);
                }
                ;


block           : ^(BLOCK statement*)
                ;


statement       : block
                | ifCall
                | assignmentExpression
                | ^(WHILE loopCondition statement)
                | ^(UNTIL loopCondition statement)
                | foreachLoop
                | ^(BREAKIF expression)
                | BREAK
                | expression
                ;


ifCall          : ^(IF expression statement statement?)
                ;


foreachLoop
@init {
    varScope.addLevel("foreach");
}
@after {
    varScope.dropLevel();
}
                : ^(FOREACH ID {varScope.addSymbol($ID.text, SymbolType.LOOP_VAR, ScopeType.PIXEL);} loopSet statement)
                ;


loopCondition   : expression
                ;


loopSet         : ^(SEQUENCE expression expression)
                | listLiteral
                | VAR_LIST
                ;


expressionList  : ^(EXPR_LIST expression*)
                ;


assignmentExpression
                : ^(assignmentOp identifier expression)
                { 
                    String varName = $identifier.start.getText();
                    int idtype = $identifier.start.getType();
                    switch (idtype) {
                        case CONSTANT:
                            msgTable.add(varName, Message.CONSTANT_LHS);
                            break;

                        case VAR_SOURCE:
                            msgTable.add(varName, Message.ASSIGNMENT_TO_SRC_IMAGE); 
                            break;

                        default:
                            if ($assignmentOp.start.getType() == EQ) {
                                if (!varScope.isDefined(varName)) {
                                    varScope.addSymbol(varName, SymbolType.SCALAR, ScopeType.PIXEL);
                                }

                            } else {
                                switch (idtype) {
                                    case VAR_DEST:
                                        msgTable.add(varName, Message.INVALID_ASSIGNMENT_OP_WITH_DEST_IMAGE);
                                        break;

                                    default:
                                        if (!varScope.isDefined(varName)) {
                                            msgTable.add(varName, Message.UNINIT_VAR);
                                        }
                                }
                            }
                    }
                }
                ;


assignmentOp    : EQ
                | TIMESEQ
                | DIVEQ
                | MODEQ
                | PLUSEQ
                | MINUSEQ
                ;


identifier      : imageVar
                | userVar
                | CONSTANT
                ;


imageVar        : VAR_SOURCE
                | VAR_DEST
                ;

userVar         : VAR_IMAGE_SCOPE
                | VAR_PIXEL_SCOPE
                | VAR_LOOP
                | VAR_LIST
                ;


expression      : ^(FUNC_CALL ID expressionList)
                | ^(CON_CALL expressionList)
                | ^(QUESTION expression expression expression)
                | ^(IMAGE_WRITE . expression)
                | ^(IMAGE_POS . bandSpecifier? pixelSpecifier?)
                | ^(logicalOp expression expression)
                | ^(arithmeticOp expression expression)
                | ^(POW expression expression)
                | ^(PREFIX prefixOp expression)
                | ^(POSTFIX incdecOp expression)
                | ^(PAR expression)
                | listOperation
                | listLiteral
                | literal
                | imageVar
                | CONSTANT

                | userVar
                {
                    String varName = $userVar.start.getText();
                    int varType = $userVar.start.getType();
                    if (!varScope.isDefined(varName)) {
                        msgTable.add(varName, Message.UNINIT_VAR);
                    }
                }
                ;


listOperation   : ^(APPEND VAR_LIST expression)
                {
                    if (!varScope.isDefined($VAR_LIST.text)) {
                        msgTable.add($VAR_LIST.text, Message.UNDECLARED_LIST_VAR);
                    }
                }
                ;


listLiteral     : ^(DECLARED_LIST expressionList)
                ;


logicalOp       : OR
                | XOR
                | AND
                | LOGICALEQ
                | NE
                | GT
                | GE
                | LT
                | LE
                ;


arithmeticOp    : PLUS
                | MINUS
                | TIMES
                | DIV
                | MOD
                ;


prefixOp        : PLUS
                | MINUS
                | NOT
                | incdecOp
                ;


incdecOp        : INCR
                | DECR
                ;


pixelSpecifier  : ^(PIXEL_REF pixelPos pixelPos)
                ;


bandSpecifier   : ^(BAND_REF expression)
                ;


pixelPos        : ^(ABS_POS expression)
                | ^(REL_POS expression)
                ;


literal         : INT_LITERAL
                | FLOAT_LITERAL
                | TRUE
                | FALSE
                | NULL
                ;
