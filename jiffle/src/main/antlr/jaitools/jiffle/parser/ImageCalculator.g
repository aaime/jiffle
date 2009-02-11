/*
 * Copyright 2009 Michael Bedward
 * 
 * This file is part of jai-tools.

 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.

 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
 
 /** 
  *
  * @author Michael Bedward
  */

tree grammar ImageCalculator;

options {
    tokenVocab = ExpressionSimplifier;
    ASTLabelType = CommonTree;
}

@header {
package jaitools.jiffle.parser;
}

@members {
private boolean printDebug = false;
public void setPrint(boolean b) { printDebug = b; }
}

start           : (statement)+
                ;

statement       : image_write
                | var_assignment
                ;

image_write     : ^(IMAGE_WRITE IMAGE_VAR expr)
                ;

var_assignment  : ^(ASSIGN assign_op var expr)
                ;
                
expr            : ^(SIMPLE_EXPR expr)
                | ^(FUNC_CALL ID expr_list)
                | ^(QUESTION expr expr expr)
                | ^(expr_op expr expr)
                | var
                | INT_LITERAL 
                | FLOAT_LITERAL 
                ;
                
                
expr_list       : ^(EXPR_LIST expr*)
                ;
                
var             : POS_VAR
                | SIMPLE_VAR
                | IMAGE_VAR
                ;
                
expr_op         : POW
                | TIMES 
                | DIV 
                | MOD
                | PLUS  
                | MINUS
                | OR 
                | AND 
                | XOR 
                | GT 
                | GE 
                | LE 
                | LT 
                | LOGICALEQ 
                | NE 
                ;

assign_op	: EQ
		| TIMESEQ
		| DIVEQ
		| MODEQ
		| PLUSEQ
		| MINUSEQ
		;
		
incdec_op       : INCR
                | DECR
                ;

unary_op	: PLUS
		| MINUS
		| NOT
		;
		
type_name	: 'int'
		| 'float'
		| 'double'
		| 'boolean'
		;
