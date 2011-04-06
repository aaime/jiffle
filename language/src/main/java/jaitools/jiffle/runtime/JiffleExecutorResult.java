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

import java.awt.image.RenderedImage;
import java.util.Map;

import jaitools.jiffle.Jiffle;

/**
 * Used by {@link JiffleExecutor} to send the results of a task to 
 * {@link JiffleEventListener}s.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class JiffleExecutorResult {

    private final int jobID;
    private final JiffleDirectRuntime runtime;
    private final boolean completed;

    /**
     * Creates a new result object.
     * 
     * @param taskID the task ID assigned by the executor
     * @param runtime the run-time instance
     * @param completed whether the task was completed successfully
     */
    public JiffleExecutorResult(int taskID, JiffleDirectRuntime runtime, boolean completed) {
        this.jobID = taskID;
        this.runtime = runtime;
        this.completed = completed;
    }

    /**
     * Gets source and/or destination images that were used.
     * 
     * @return the images keyed by script variable name
     */
    public Map<String, RenderedImage> getImages() {
        return runtime.getImages();
    }

    /**
     * Gets the {@link Jiffle} object
     * 
     * @return the {@link Jiffle} object
     */
    public JiffleDirectRuntime getRuntime() {
        return runtime;
    }

    /**
     * Gets the task ID assigned by the executor.
     * 
     * @return task ID
     */
    public int getTaskID() {
        return jobID;
    }

    /**
     * Gets the completion status of the task.
     * 
     * @return {@code true} if the task was completed; {@code false} otherwise
     */
    public boolean isCompleted() {
        return completed;
    }
    
}
