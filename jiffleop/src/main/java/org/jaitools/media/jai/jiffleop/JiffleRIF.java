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

package org.jaitools.media.jai.jiffleop;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import java.util.Map;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;

import org.jaitools.CollectionFactory;

/**
 * The image factory for the "Jiffle" operation.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class JiffleRIF implements RenderedImageFactory {

    /** Constructor */
    public JiffleRIF() {
    }

    /**
     * Create a new instance of JiffleOpImage in the rendered layer.
     *
     * @param paramBlock specifies the source image and the parameters
     * WRITE ME
     */
    public RenderedImage create(ParameterBlock paramBlock,
            RenderingHints renderHints) {
        
        Map<String, RenderedImage> sourceImages = CollectionFactory.map();
        
        String script = (String) paramBlock.getObjectParameter(JiffleDescriptor.SCRIPT_ARG);
        String destVarName = (String) paramBlock.getObjectParameter(JiffleDescriptor.DEST_NAME_ARG);
        Rectangle destBounds = (Rectangle) paramBlock.getObjectParameter(JiffleDescriptor.DEST_BOUNDS_ARG);

        // Ignore any ImageLayout that was provided and create one here
        ImageLayout layout = new ImageLayout(destBounds.x, destBounds.y, destBounds.width, destBounds.height);

        Dimension defaultTileSize = JAI.getDefaultTileSize();
        SampleModel sm = RasterFactory.createPixelInterleavedSampleModel(
                DataBuffer.TYPE_DOUBLE, defaultTileSize.width, defaultTileSize.height, 1);
        layout.setSampleModel(sm);
        layout.setColorModel(PlanarImage.createColorModel(sm));
        
        return new JiffleOpImage(sourceImages, layout, renderHints, script, destVarName, destBounds);
    }
}

