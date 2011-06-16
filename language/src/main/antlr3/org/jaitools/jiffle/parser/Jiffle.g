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

/**
 * Jiffle language parser grammar. Generates the primary AST
 * from an input Jiffle script.
 *
 * @author Michael Bedward
 */

grammar Jiffle;

options {
    output=AST;
    ASTLabelType = CommonTree;
    backtrack = true;
    memoize = true;
}

tokens {
    ABS_POS;
    BAND_REF;
    BLOCK;
    CON_CALL;
    DECL;
    DECLARED_LIST;
    EXPR_LIST;
    FUNC_CALL;
    IMAGE_POS;
    JIFFLE_OPTION;
    PAR;
    PIXEL_REF;
    POSTFIX;
    PREFIX;
    REL_POS;
    SEQUENCE;
    VAR_DEST;
    VAR_IMAGE_SCOPE;
    VAR_SOURCE;

    // Used by later tree parsers
    CONSTANT;
    IMAGE_WRITE;
    LIST_NEW;
    VAR_IMAGE;
    VAR_PIXEL_SCOPE;
    VAR_PROVIDED;
    VAR_LOOP;
    VAR_LIST;
}

@header {
package org.jaitools.jiffle.parser;

import java.util.Map;
import org.jaitools.CollectionFactory;
import org.jaitools.jiffle.Jiffle;
}

@lexer::header {
package org.jaitools.jiffle.parser;
}

@members {

@Override
protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
    if (ttype == Token.EOF) {
        throw new UnexpectedInputException("Invalid statement before end of file");
    }
    return super.recoverFromMismatchedToken(input, ttype, follow);
}

private List<Integer> blocksFound = CollectionFactory.list();

private void checkBlock(Token blockToken) {
    if ( blocksFound.contains(blockToken.getType()) ) {
        throw new JiffleParserException("Duplicate " + blockToken.getText() + " block");
    }

    blocksFound.add(blockToken.getType());
}

private Map<String, Jiffle.ImageRole> imageParams = CollectionFactory.map();

private void setImageVar(String varName, int type) {
    Jiffle.ImageRole role = null;
    switch (type) {
        case READ:
            role = Jiffle.ImageRole.SOURCE;
            break;

        case WRITE:
            role = Jiffle.ImageRole.DEST;
            break;

        default:
            throw new IllegalArgumentException("type must be READ or WRITE");
    }

    imageParams.put(varName, role);
}

public Map<String, Jiffle.ImageRole> getImageParams() { return imageParams; }

}


prog            : (blk=specialBlock {checkBlock($blk.start);} )* statement+ EOF!
                ;
                catch [UnexpectedInputException ex] {
                    throw new JiffleParserException(ex);
                }
                catch [EarlyExitException ex] {
                    throw new JiffleParserException("Unexpected input at line " + ex.line);
                }


specialBlock    : optionsBlock
                | imagesBlock
                | initBlock
                ;


optionsBlock    : OPTIONS LCURLY option* RCURLY -> option*
                ;

option          : ID EQ optionValue SEMI -> ^(JIFFLE_OPTION ID optionValue)
                ;


optionValue     : ID
                | literal
                ;


// No AST output
imagesBlock     : IMAGES LCURLY imageVarDeclaration* RCURLY -> 
                ;


imageVarDeclaration
                : ID EQ role SEMI
                { setImageVar($ID.text, $role.start.getType()); }
                ;


role            : READ
                | WRITE
                ;


initBlock       : INIT LCURLY varDeclaration* RCURLY -> varDeclaration*
                ;


varDeclaration  : ID (EQ expression)? SEMI -> ^(DECL VAR_IMAGE_SCOPE ID expression?)
                ;


block           : LCURLY statement* RCURLY -> ^(BLOCK statement*)
                ;


statement       : ifCall
                | block
                | delimitedStatement SEMI!
                | assignmentExpression SEMI!
                | WHILE LPAR loopCondition RPAR statement -> ^(WHILE loopCondition statement)
                | UNTIL LPAR loopCondition RPAR statement -> ^(UNTIL loopCondition statement)
                | FOREACH LPAR ID IN loopSet RPAR statement -> ^(FOREACH ID loopSet statement)
                | SEMI!
                ;


ifCall          : IF LPAR orExpression RPAR s1=statement
                  ( ELSE s2=statement -> ^(IF orExpression $s1 $s2)
                  | -> ^(IF orExpression $s1)
                  )
                ;


delimitedStatement
                : expression
                | BREAKIF LPAR expression RPAR -> ^(BREAKIF expression)
                | BREAK
                ;


loopCondition   : orExpression
                ;


loopSet         : listLiteral
                | sequence
                | ID
                ;


expressionList  : (expression (COMMA expression)* )? -> ^(EXPR_LIST expression*)
                ;


sequence        : lo=expression COLON hi=expression -> ^(SEQUENCE $lo $hi)
                ;


/*
 * The "con" function is treated separately from general
 * functions during compilation because we want to ensure lazy evaluation
 * of the alternatives.
 */
conCall         : CON LPAR expressionList RPAR -> ^(CON_CALL expressionList)
                ;


/*
 * Assignment expressions are treated as a special case, outside of the general
 * expression rule hierarchy. This allows ANTLR to cope with the grammar without
 * requiring backtracking.
 */
assignmentExpression
                : ID assignmentOp^ expression
                ;

expression      : conditionalExpression
                | ID APPEND^ expression
                ;


assignmentOp    : EQ
                | TIMESEQ
                | DIVEQ
                | MODEQ
                | PLUSEQ
                | MINUSEQ
                ;


conditionalExpression
                : orExpression (QUESTION^ expression COLON! expression)?
                ;


orExpression    : xorExpression (OR^ xorExpression)*
                ;


xorExpression   : andExpression (XOR^ andExpression)*
                ;


andExpression   : eqExpression (AND^ eqExpression)*
                ;


eqExpression    : compExpression ((LOGICALEQ^ | NE^) compExpression)?
                ;


compExpression  : addExpression ((GT^ | GE^ | LE^ | LT^) addExpression)?
                ;


addExpression   : multExpression ((PLUS^ | MINUS^) multExpression)*
                ;


multExpression  : unaryExpression ((TIMES^ | DIV^ | MOD^) unaryExpression)*
                ;


unaryExpression : prefixOp unaryExpression -> ^(PREFIX prefixOp unaryExpression)
                | powerExpression
                ;


prefixOp        : PLUS
                | MINUS
                | NOT
                | incdecOp
                ;


incdecOp        : INCR
                | DECR
                ;


powerExpression : primaryExpression (POW^ primaryExpression)*
                ;


primaryExpression
@init { boolean postfix = false; }
                : atom (incdecOp { postfix = true; } )?
                  -> {postfix}? ^(POSTFIX incdecOp atom)
                  -> atom
                ;


atom            : LPAR expression RPAR -> ^(PAR expression)
                | literal
                | listLiteral
                | conCall
                | identifiedAtom
                ;


identifiedAtom  : ID arguments -> ^(FUNC_CALL ID arguments)
                | ID imagePos -> ^(IMAGE_POS ID imagePos)
                | ID
                ;


arguments       : LPAR! expressionList RPAR!
                ;


imagePos        : bandSpecifier pixelSpecifier
                | pixelSpecifier
                | bandSpecifier
                ;


pixelSpecifier  : LSQUARE pixelPos COMMA pixelPos RSQUARE -> ^(PIXEL_REF pixelPos pixelPos)
                ;


bandSpecifier   : LSQUARE expression RSQUARE -> ^(BAND_REF expression)
                ;


pixelPos        : ABS_POS_PREFIX expression -> ^(ABS_POS expression)
                | expression -> ^(REL_POS expression)
                ;


literal         : INT_LITERAL
                | FLOAT_LITERAL
                | TRUE
                | FALSE
                | NULL
                ;


listLiteral     : LSQUARE expressionList RSQUARE -> ^(DECLARED_LIST expressionList)
                ;


/////////////////////////////////////////////////
// Lexer rules
/////////////////////////////////////////////////

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

/* Logical constants */
TRUE    : 'TRUE' | 'true' ;
FALSE   : 'FALSE' | 'false' ;
NULL    : 'NULL' | 'null' ;

/* Keywords */
INT_TYPE        : 'int' ;
FLOAT_TYPE      : 'float' ;
DOUBLE_TYPE     : 'double' ;
BOOLEAN_TYPE    : 'boolean' ;

OPTIONS : 'options' ;
IMAGES  : 'images' ;
INIT    : 'init' ;
READ    : 'read' ;
WRITE   : 'write' ;

CON     : 'con' ;
IF      : 'if' ;
ELSE    : 'else' ;
WHILE   : 'while' ;
UNTIL   : 'until' ;
FOREACH : 'foreach' ;
IN      : 'in' ;
BREAKIF : 'breakif' ;
BREAK   : 'break' ;

/* Operators sorted and grouped by precedence order */

ABS_POS_PREFIX
        : '$'  ;

APPEND  : '<<' ;

INCR    : '++' ;
DECR    : '--' ;

NOT     : '!' ;
POW     : '^' ;
TIMES   : '*' ;
DIV     : '/' ;
MOD     : '%' ;
PLUS    : '+' ;
MINUS   : '-' ;
GT      : '>';
GE      : '>=';
LE      : '<=';
LT      : '<';
LOGICALEQ : '==';
NE      : '!=';
AND     : '&&';
OR      : '||';
XOR     : '^|';
QUESTION: '?' ;  /* ternary conditional operator ?: */
TIMESEQ : '*=' ;
DIVEQ   : '/=' ;
MODEQ   : '%=' ;
PLUSEQ  : '+=' ;
MINUSEQ : '-=' ;
EQ      : '='  ;

/* General tokens */
COMMA   : ',' ;
SEMI    : ';' ;
COLON   : ':' ;
LPAR    : '(' ;
RPAR    : ')' ;
LSQUARE : '[' ;
RSQUARE : ']' ;
LCURLY  : '{' ;
RCURLY  : '}' ;

ID      : (Letter) (Letter | UNDERSCORE | Digit | Dot)*
        ;


fragment
Letter  : 'a'..'z' | 'A'..'Z'
        ;

UNDERSCORE
        : '_' ;

INT_LITERAL
        : '0' | NonZeroDigit Digit*
        ;

FLOAT_LITERAL
        : ('0' | NonZeroDigit Digit*)? Dot Digit* FloatExp?
        ;

fragment
Digit   : '0'..'9' ;

fragment
Dot     : '.' ;

fragment
NonZeroDigit
        : '1'..'9'
        ;

fragment
FloatExp
        : ('e'|'E' (PLUS|MINUS)? '0'..'9'+)
        ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        | '\u000C'
        ) {$channel=HIDDEN;}
    ;


/* 
 * The following are for future use 
 */

CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
    ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
