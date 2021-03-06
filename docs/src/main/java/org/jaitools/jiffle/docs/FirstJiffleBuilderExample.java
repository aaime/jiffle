package org.jaitools.jiffle.docs;

import java.awt.image.RenderedImage;
import java.io.File;

import org.jaitools.jiffle.JiffleBuilder;
import org.jaitools.jiffle.JiffleException;

public class FirstJiffleBuilderExample {
    
    // docs start method
    public RenderedImage buildAndRunScript(File scriptFile, RenderedImage inputImage) 
            throws JiffleException {
        
        JiffleBuilder builder = new JiffleBuilder();
        
        builder.script(scriptFile).source("src", inputImage);
        builder.dest("dest", inputImage.getWidth(), inputImage.getHeight());
        builder.run();
        
        return builder.getImage("dest");
    }
    // docs end method
    
}
