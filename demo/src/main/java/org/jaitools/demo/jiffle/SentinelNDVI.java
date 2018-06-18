package org.jaitools.demo.jiffle;

import com.sun.media.jai.operator.ImageReadDescriptor;

import org.jaitools.imageutils.ImageUtils;
import org.jaitools.jiffle.JiffleBuilder;
import org.jaitools.jiffle.JiffleException;
import org.jaitools.jiffle.runtime.AbstractProgressListener;
import org.jaitools.jiffle.runtime.JiffleDirectRuntime;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.TiledImage;

public class SentinelNDVI {

    public static void main(String[] args) throws IOException, JiffleException {
        String basePath =
                "/opt/gisData/evo-odas/coverave-view-hetero/multires-s2/S2A_MSIL1C_20170410T103021_N0204_R108_T32UNU_20170410T103020.SAFE/20170410T103021026Z_fullres_CC2.4251_T32UNU_";

        // inputs are large, give it some tile cache space
        TileCache tc = JAI.getDefaultInstance().getTileCache();
        tc.setMemoryCapacity(1024 * 1024 * 1024);

        // prepare inputs and outputs
        RenderedImage red = readImage(new File(basePath + "B04.tif"));
        RenderedImage nir = readImage(new File(basePath + "B08.tif"));
        TiledImage result =
                ImageUtils.createConstantImage(red.getWidth(), red.getHeight(), (float) 0);

        // build the operation
        JiffleBuilder builder = new JiffleBuilder();
        builder.dest("res", result).source("red", red).source("nir", nir);
        builder.script("n = nir; r = red; res = (n - r) / (n + r);"); // HERE IS THE NDVI SCRIPT! 5.5 seconds!
        // builder.script("res = (nir - red) / (nir + red);"); // HERE IS THE NDVI SCRIPT! 7.65 sec!
        JiffleDirectRuntime runtime = builder.getRuntime();

        // actually running the calculation
        double pixels = (double) red.getWidth() * (double) red.getHeight();
        System.out.println(
                "Computing " + NumberFormat.getNumberInstance().format(pixels) + " pixels");
        long start = System.currentTimeMillis();
        runtime.evaluateAll(
                new AbstractProgressListener() {
                    int nextUpdate = 0;

                    @Override
                    public void start() {
                        System.out.println("Computation starts");
                    }

                    @Override
                    public void update(long done) {
                        double perc = done / pixels * 100;
                        if (perc > nextUpdate) {
                            System.out.println(nextUpdate++);
                        }
                    }

                    @Override
                    public void finish() {
                        System.out.println("Computation ends");
                    }
                });
        long end = System.currentTimeMillis();
        System.out.println("Computation of output took " + (end - start) / 1000.0 + " secs");
        System.out.println("Writing output to disk");
        ImageIO.write(result, "TIF", new File("/tmp/ndvi.tif"));
        System.out.println("Writing complete");
    }

    private static RenderedOp readImage(File file) throws IOException {
        FileImageInputStream stream = new FileImageInputStream(file);
        ImageReader reader = ImageIO.getImageReaders(stream).next();
        return ImageReadDescriptor.create(
                stream, 0, false, false, false, null, null, null, reader, null);
    }
}
