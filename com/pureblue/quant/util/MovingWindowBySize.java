/*
 * Copyright (c) Numerical Method Inc.
 * http://www.numericalmethod.com/
 * 
 * THIS SOFTWARE IS LICENSED, NOT SOLD.
 * 
 * YOU MAY USE THIS SOFTWARE ONLY AS DESCRIBED IN THE LICENSE.
 * IF YOU ARE NOT AWARE OF AND/OR DO NOT AGREE TO THE TERMS OF THE LICENSE,
 * DO NOT USE THIS SOFTWARE.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITH NO WARRANTY WHATSOEVER,
 * EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION,
 * ANY WARRANTIES OF ACCURACY, ACCESSIBILITY, COMPLETENESS,
 * FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT, 
 * TITLE AND USEFULNESS.
 * 
 * IN NO EVENT AND UNDER NO LEGAL THEORY,
 * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
 * ARISING AS A RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.pureblue.quant.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an efficient implementation of a moving window of data, without the need to do
 * rollover by copying.
 *
 * @param <T> data type
 * @author Haksun Li
 */
public class MovingWindowBySize<T> {

    private final int size;
    private final ArrayList<T> queue = new ArrayList<>(); // TODO: use ArrayBlockingQueue?

    public MovingWindowBySize(int size) {
        this.size = size;
    }

    public int size() {
        return size;
    }

    public void add(T t) {
        if (queue.size() >= size) {
            queue.remove(0); // roll over by pointers, discarding the oldest row
        }
        queue.add(t);
    }

    public boolean isReady() {
        return queue.size() >= size;
    }

    /**
     * Gets the window data.
     *
     * @return the window data
     */
    public List<T> asList() {
        List<T> list = new ArrayList<>();
        list.addAll(queue);

        return list;
    }
}
