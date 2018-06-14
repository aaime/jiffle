/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.jaitools.jiffle.parser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Compares two java sources
 *
 * @author Andrea Aime - GeoSolutions
 * @source $URL$
 */
public class SourceAssert {

    /**
     * Makes the test interactive, showing a Swing dialog with before/after and a choice to
     * overwrite the expected image
     */
    static final boolean INTERACTIVE = Boolean.getBoolean("interactive");

    static final Logger LOGGER = Logger.getLogger("SourceAssert");

    public static void compare(File expectedFile, String actualSource)
            throws IOException {
        // do we have the reference source at all?
        if (!expectedFile.exists()) {

            // see what the user thinks of the image
            boolean useAsReference = INTERACTIVE && ReferenceSourceDialog.show(actualSource);
            if (useAsReference) {
                try {
                    File parent = expectedFile.getParentFile();
                    if (!parent.exists() && !parent.mkdirs()) {
                        throw new AssertionError(
                                "Could not create directory that will contain :"
                                        + expectedFile.getParent());
                    }
                    FileUtils.writeStringToFile(expectedFile, actualSource);
                } catch (IOException e) {
                    throw (Error)
                            new AssertionError("Failed to write the source to disk").initCause(e);
                }
            } else {
                throw new AssertionError(
                        "Reference source is missing: "
                                + expectedFile
                                + ", add -Dinteractive=true to show a dialog comparing them (requires GUI support)");
            }
        } else {
            String expectedSource = FileUtils.readFileToString(expectedFile);
            if (!expectedSource.equals(actualSource)) {
                // check with the user
                boolean overwrite = false;
                if (INTERACTIVE) {
                    overwrite = CompareSourceDialog.show(expectedSource, actualSource, true);
                } else {
                    LOGGER.info(
                            "Sources are different, add -interactive=true to show a dialog comparing them (requires GUI support)");
                }

                if (overwrite) {
                    FileUtils.writeStringToFile(expectedFile, actualSource);
                } else {
                    throw new AssertionError(
                            "Sources are different. \nYou can add -Dinteractive=true to show a dialog comparing them (requires GUI support)");
                }
            }
        }
    }
}
