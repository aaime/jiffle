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

import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Map;
import java.util.Vector;

import javax.media.jai.ImageLayout;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;

import org.jaitools.CollectionFactory;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.JiffleException;
import org.jaitools.jiffle.runtime.JiffleIndirectRuntime;

/**
 * Jiffle operation.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class JiffleOpImage extends OpImage {
    
    private final JiffleIndirectRuntime runtime;
    
    // TESTING
    private final int band = 0;
    
    private final Rectangle bounds;

    public JiffleOpImage(Map<String, RenderedImage> sourceImages, 
            ImageLayout layout, 
            Map configuration,
            String script,
            String destVarName,
            Rectangle destBounds) {
        
        super(new Vector(sourceImages.values()), layout, configuration, false);
        
        try {
            Jiffle jiffle = new Jiffle();
            jiffle.setScript(script);
            
            Map<String, Jiffle.ImageRole> imageParams = CollectionFactory.map();
            for (String varName : sourceImages.keySet()) {
                imageParams.put(varName, Jiffle.ImageRole.SOURCE);
            }
            imageParams.put(destVarName, Jiffle.ImageRole.DEST);
            
            jiffle.setImageParams(imageParams);
            jiffle.compile();
            runtime = (JiffleIndirectRuntime) jiffle.getRuntimeInstance(Jiffle.RuntimeModel.INDIRECT);
            
            for (Map.Entry<String, RenderedImage> entry : sourceImages.entrySet()) {
                runtime.setSourceImage(entry.getKey(), entry.getValue());
            }
            
            if (destBounds == null) {
                bounds = getSourceBounds();
                if (bounds == null) {
                    throw new IllegalArgumentException(
                            "No source images and no destination bounds specified");
                }
            } else {
                bounds = new Rectangle(destBounds);
            }
            
            runtime.setWorldByResolution(bounds, 1, 1);
            
        } catch (JiffleException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    

    /**
     * For testing: returns null to indicate that all of the destination
     * could be affected.
     * 
     * @param sourceRect
     * @param sourceIndex
     * @return 
     */
    @Override
    public Rectangle mapSourceRect(Rectangle sourceRect, int sourceIndex) {
        return null;
    }

    /**
     * For testing: returns the source image bounds.
     * 
     * @param destRect
     * @param sourceIndex
     * @return 
     */
    @Override
    public Rectangle mapDestRect(Rectangle destRect, int sourceIndex) {
        return getSourceImage(sourceIndex).getBounds();
    }

    @Override
    protected void computeRect(PlanarImage[] sources, WritableRaster dest, Rectangle destRect) {
        for (int y = destRect.y, iy = 0; iy < destRect.height; y++, iy++) {
            for (int x = destRect.x, ix = 0; ix < destRect.width; x++, ix++) {
                dest.setSample(x, y, band, runtime.evaluate(x, y));
            }
        }
    }

    @Override
    protected void computeRect(Raster[] sources, WritableRaster dest, Rectangle destRect) {
        super.computeRect(sources, dest, destRect);
    }

    @Override
    public Raster computeTile(int tileX, int tileY) {
        return super.computeTile(tileX, tileY);
    }
    
    

    private Rectangle getSourceBounds() {
        Rectangle r = null;
        
        if (getNumSources() > 0) {
            r = new Rectangle(getSourceImage(0).getBounds());
            
            for (int i = 1; i < getNumSources(); i++) {
                r = r.union(getSourceImage(i).getBounds());
            }
        }
        
        return r;
    }

    
}
