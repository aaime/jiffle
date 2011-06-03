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

package jaitools.jiffle.runtime;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import jaitools.CollectionFactory;


/**
 * An event listener that uses a {@code CountDownLatch} to force the client to
 * wait for the expected number of tasks to be completed.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class WaitingListener implements JiffleEventListener {

    private CountDownLatch latch = null;

    private final List<JiffleExecutorResult> results = CollectionFactory.list();
    
    /**
     * Sets the number of task completions and/or failures to wait for.
     * 
     * @param n number of tasks
     */
    public void setNumTasks(int n) {
        if (latch != null && latch.getCount() > 0) {
            throw new IllegalStateException("Method called during wait period");
        }

        latch = new CountDownLatch(n);
    }
    
    /**
     * Waits for tasks to finish.
     */
    public boolean await(long timeOut, TimeUnit units) {
        if (latch == null) {
            throw new RuntimeException("Called await without setting number of tasks");
        }
        
        try {
            boolean isZero = latch.await(timeOut, units);
            if (!isZero) {
                return false;
            }
            return true;
            
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public List<JiffleExecutorResult> getResults() {
        return results;
    }

    public void onCompletionEvent(JiffleEvent ev) {
        JiffleExecutorResult result = ev.getResult();
        results.add(result);
        latch.countDown();
    }

    public void onFailureEvent(JiffleEvent ev) {
        JiffleExecutorResult result = ev.getResult();
        results.add(result);
        latch.countDown();
    }

}
