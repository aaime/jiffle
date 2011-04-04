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

package jaitools.jiffle.runtime;

import jaitools.jiffle.JiffleException;
import org.junit.Test;

/**
 * Unit tests for Jiffle's loop statements.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class LoopTest extends StatementsTestBase {

    @Test
    public void whileLoopWithSimpleStatement() throws Exception {
        System.out.println("   while loop with simple statement");
        String script = 
                  "n = 0; \n"
                + "while (n < x()) n++; \n"
                + "dest = n;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;
            
            public double eval(double val) {
                int xx = x;
                x = (x + 1) % IMG_WIDTH;
                return xx;
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void whileLoopWithBlock() throws Exception {
        System.out.println("   while loop with block");
        String script = 
                  "n = 0; \n"
                + "i = 0; \n"
                + "while (i < x()) { n += i; i++ ; } \n"
                + "dest = n;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;
            public double eval(double val) {
                int n = 0;
                for (int i = 0; i < x; i++) n += i;
                x = (x + 1) % IMG_WIDTH;
                return n;
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void untilLoopWithSimpleStatement() throws Exception {
        System.out.println("   until loop with simple statement");
        String script = 
                  "n = 0; \n"
                + "until (n > x()) n++; \n"
                + "dest = n;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;
            
            public double eval(double val) {
                int xx = x;
                x = (x + 1) % IMG_WIDTH;
                return xx + 1;
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void untilLoopWithBlock() throws Exception {
        System.out.println("   until loop with block");
        String script = 
                  "n = 0; \n"
                + "i = 0; \n"
                + "until (i > x()) { n += i; i++ ; } \n"
                + "dest = n;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;
            public double eval(double val) {
                int n = 0;
                for (int i = 0; i <= x; i++) n += i;
                x = (x + 1) % IMG_WIDTH;
                return n;
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void foreachListLoopWithSimpleStatement() throws Exception {
        System.out.println("   foreach (i in [x(), y(), 3]) simple statement");
        String script =
                  "z = 0;"
                + "foreach (i in [x(), y(), 3]) z += i;"
                + "dest = z;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;
            int y = 0;

            public double eval(double val) {
                double z = x + y + 3;
                x = (x + 1) % IMG_WIDTH;
                if (x == 0) y++ ;
                return z;
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void foreachListLoopWithBlock() throws Exception {
        System.out.println("   foreach (i in [x(), y(), 3]) block");
        String script =
                  "z = 0;"
                + "foreach (i in [x(), y(), 3]) \n"
                + "{ \n"
                + "    temp = i * 2; \n"
                + "    z += temp; \n"
                + "} \n"
                + "dest = z;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;
            int y = 0;

            public double eval(double val) {
                double z = 2*(x + y + 3);
                x = (x + 1) % IMG_WIDTH;
                if (x == 0) y++ ;
                return z;
            }
        };
        
        testScript(script, e);
    }

    @Test
    public void foreachSequenceLoopWithSimpleStatement() throws Exception {
        System.out.println("   foreach (i in -1:5) simple statement");
        String script =
                  "z = 0; \n"
                + "foreach (i in -1:5) z += i*src; \n"
                + "dest = z;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;

            public double eval(double val) {
                double z = 0;
                for (int i = -1; i <= 5; i++) z += val * i;
                x = (x + 1) % IMG_WIDTH;
                return z;
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void foreachSequenceLoopWithBlock() throws Exception {
        System.out.println("   foreach (i in -1:5) block");
        String script =
                  "z = 0; \n"
                + "foreach (i in -1:5) { \n"
                + "    temp = i * src; \n"
                + "    z += temp; \n"
                + "} \n"
                + "dest = z;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;

            public double eval(double val) {
                double z = 0;
                for (int i = -1; i <= 5; i++) z += val * i;
                x = (x + 1) % IMG_WIDTH;
                return z;
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void breakif() throws Exception {
        System.out.println("   breakif");
        String script = 
                  "n = 0; \n"
                + "i = 0; \n"
                + "while (i < x()) { \n"
                + "  n += i; \n"
                + "  breakif(n >= 10); \n"
                + "  i++ ; \n"
                + "} \n"
                + "dest = n;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;
            public double eval(double val) {
                int n = 0;
                for (int i = 0; i < x; i++) n += i;
                x = (x + 1) % IMG_WIDTH;
                return (n < 10 ? n : 10);
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void breakifNestedInIf() throws Exception {
        System.out.println("   breakif nested in if-block");
        
        String script = 
                  "n = 0; \n"
                + "i = 0; \n"
                + "while (i < x()) { \n"
                + "  n += i; \n"
                + "  if (true) { \n"
                + "      breakif(n >= 10); \n"
                + "  } \n"
                + "  i++ ; \n"
                + "} \n"
                + "dest = n;" ;
        
        Evaluator e = new Evaluator() {
            int x = 0;
            public double eval(double val) {
                int n = 0;
                for (int i = 0; i < x; i++) n += i;
                x = (x + 1) % IMG_WIDTH;
                return (n < 10 ? n : 10);
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void unconditionalBreak() throws Exception {
        System.out.println("   unconditional break");
        String script = 
                  "i = 0;"
                + "while (i < src) { \n"
                + "  if (++i >= 5) break;"
                + "} \n"
                + "dest = i;";
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return Math.min(val, 5.0);
            }
        };
        
        testScript(script, e);
    }
    
    @Test(expected=JiffleException.class)
    public void breakifStatementOutsideOfLoop() throws Exception {
        System.out.println("   breakif statement outside loop throws exception");
        String script = 
                  "i = 42;\n"
                + "breakif( i == 42 );\n"
                + "dest = i;" ;

        Evaluator e = new Evaluator() {
            public double eval(double val) {
                throw new IllegalStateException("Should not be called");
            }
        };
        
        testScript(script, e);
    }
    
    @Test(expected=JiffleException.class)
    public void breakStatementOutsideOfLoop() throws Exception {
        System.out.println("   break statement outside loop throws exception");
        String script = 
                  "i = 42;\n"
                + "break;\n"
                + "dest = i;" ;

        Evaluator e = new Evaluator() {
            public double eval(double val) {
                throw new IllegalStateException("Should not be called");
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void nestedForEachLoops() throws Exception {
        System.out.println("   nested foreach loops");
        String script = 
                  "n = 0;"
                + "foreach (i in 1:5) { \n"
                + "  foreach (j in i:(i+5)) { \n"
                + "    n += i + j; \n"
                + "  } \n"
                + "} \n"
                + "dest = src + n;" ;
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                double z = val;
                for (int i = 1; i <= 5; i++) {
                    for (int j = i; j <= i+5; j++) {
                        z += i + j;
                    }
                }
                return z;
            }
        };
        
        testScript(script, e);
    }

    @Test
    public void foreachLoopWithListVar() throws Exception {
        System.out.println("   using list var in foreach loop");
        String script = 
                  "options {outside = 0;} \n"
                + "foo = [-1, 0, 1]; \n"
                + "z = 0; \n"
                + "foreach (dx in foo) z += src[dx, 0]; \n"
                + "dest = z;";
        
        Evaluator e = new Evaluator() {
            int x = 0;
            public double eval(double val) {
                double z = val;
                if (x > 0) z += val - 1;
                if (x < IMG_WIDTH-1) z += val + 1;
                
                x = (x + 1) % IMG_WIDTH;
                return z;
            }
        };
        
        testScript(script, e);
    }
}
