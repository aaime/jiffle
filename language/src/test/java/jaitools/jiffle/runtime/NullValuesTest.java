/* 
 *  Copyright (c) 2009-2011, Michael Bedward. All rights reserved. 
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

package jaitools.jiffle.runtime;

import java.util.Map;
import java.util.Random;

import javax.media.jai.TiledImage;

import static org.junit.Assert.*;
import org.junit.Test;

import jaitools.CollectionFactory;
import jaitools.imageutils.ImageUtils;
import jaitools.jiffle.Jiffle;

/**
 * Unit tests for dealing with null values (NODATA) in arithmetic statements.
 * 
 * @author Michael Bedward
 */
public class NullValuesTest {
    
    private final int WIDTH = 10;

    /**
     * Tests for correct treatment of null (NaN) values in an arithmetic
     * expression.
     */
    @Test
    public void subtraction() throws Exception {
        System.out.println("   subtraction with null and non-null image values");
        assertScript("out = in1 - in2;");
    }

    /**
     * Tests for correct treatment of null (NaN) values in an arithmetic
     * expression within an if statement.
     */
    @Test
    public void subtractionWithinCon() throws Exception {
        System.out.println("   subtraction with null and non-null values within if statements");
        assertScript("out = con(in1 - in2, 1, 0, -1);");
    }


    /**
     * Run a script where the input images have null and non-null values and
     * assert that the destination image is correctly null or non-null.
     * 
     * @param script input script
     */
    private void assertScript(String script) throws Exception {

        TiledImage inImg1 = ImageUtils.createConstantImage(WIDTH, WIDTH, 0d);
        TiledImage inImg2 = ImageUtils.createConstantImage(WIDTH, WIDTH, 0d);
        createNullCombinations(inImg1, inImg2);

        TiledImage outImg = ImageUtils.createConstantImage(WIDTH, WIDTH, 0d);
        
        Map<String, Jiffle.ImageRole> imgParams = CollectionFactory.map();
        imgParams.put("in1", Jiffle.ImageRole.SOURCE);
        imgParams.put("in2", Jiffle.ImageRole.SOURCE);
        imgParams.put("out", Jiffle.ImageRole.DEST);
        
        Jiffle jiffle = new Jiffle(script, imgParams);
        JiffleDirectRuntime jr = (JiffleDirectRuntime) jiffle.getRuntimeInstance();
        
        jr.setSourceImage("in1", inImg1);
        jr.setSourceImage("in2", inImg2);
        jr.setDestinationImage("out", outImg);
        jr.evaluateAll(null);
        
        boolean b = false;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                boolean null1 = Double.isNaN(inImg1.getSampleDouble(i, j, 0));
                boolean null2 = Double.isNaN(inImg2.getSampleDouble(i, j, 0));
                boolean nullOut = Double.isNaN(outImg.getSampleDouble(i, j, 0));

                assertTrue(String.format("Failed for combination %s - %s",
                                         (null1 ? "NULL" : "NON-NULL"),
                                         (null2 ? "NULL" : "NON-NULL")),
                           nullOut == (null1 || null2));
            }
        }
    }
    
    
    /**
     * Write values to two images such that the possible permutations of
     * null and non-null values have approximately equal frequency.
     * 
     * @param inImg1 first image
     * @param inImg2 second image
     */
    private void createNullCombinations(TiledImage inImg1, TiledImage inImg2) {
        Random rand = new Random();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                switch (rand.nextInt(4)) {
                    case 0:
                        inImg1.setSample(i, j, 0, Double.NaN);
                        inImg2.setSample(i, j, 0, Double.NaN);
                        break;

                    case 1:
                        inImg1.setSample(i, j, 0, Double.NaN);
                        inImg2.setSample(i, j, 0, (double)(rand.nextInt(3) - 1));
                        break;

                    case 2:
                        inImg1.setSample(i, j, 0, (double)(rand.nextInt(3) - 1));
                        inImg2.setSample(i, j, 0, Double.NaN);
                        break;

                    case 3:
                        inImg1.setSample(i, j, 0, (double)(rand.nextInt(3) - 1));
                        inImg2.setSample(i, j, 0, (double)(rand.nextInt(3) - 1));
                        break;
                }
            }
        }
    }

}
