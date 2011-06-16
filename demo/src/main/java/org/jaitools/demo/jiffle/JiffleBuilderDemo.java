/* 
 *  Copyright (c) 2011, Michael Bedward. All rights reserved. 
 *   
 *  Redistribution and use in source and binary forms, with or without modification, 
 *  are permitted provided that the following conditions are met: 
 *   
 *  - Redistributions of source code must retain the above copyright notice, this  
 *    list of conditions and the following disclaimer. 
 *   
 *  - Redistributions in binary form must reproduce the above copyright notice, this 
 *    list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.   
 *   
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */   
package org.jaitools.demo.jiffle;

import java.io.File;

import org.jaitools.demo.ImageChoice;
import org.jaitools.jiffle.JiffleBuilder;
import org.jaitools.swing.ImageFrame;

/**
 * Demonstrates using JiffleBuilder to compile and run a script.
 * <p>
 * Jiffle saves you from having to write lots of tedious JAI and Java AWT code.<br>
 * JiffleBuilder saves you from having to write lots of tedious Jiffle code !
 * Specifically, it uses concise chained methods to set the script, associate
 * variable names with images, and optionally create an image to receive the
 * processing results.
 *
 * @author Michael Bedward
 * @since 1.1
 * @version $Id$
 */
public class JiffleBuilderDemo extends JiffleDemoBase {

    /**
     * Compiles and runs the "ripple" script using {@link JiffleBuilder}.
     * @param args ignored
     * @throws Exception if there are errors compiling the script.
     */
    public static void main(String[] args) throws Exception {
        JiffleBuilderDemo me = new JiffleBuilderDemo();
        File f = JiffleDemoHelper.getScriptFile(args, ImageChoice.RIPPLES);
        String script = JiffleDemoHelper.readScriptFile(f);
        JiffleBuilder jb = new JiffleBuilder();
        jb.script(script).dest("result", WIDTH, HEIGHT).getRuntime().evaluateAll(null);

        ImageFrame frame = new ImageFrame(jb.getImage("result"), "Jiffle image demo");
        frame.setVisible(true);
    }
}
