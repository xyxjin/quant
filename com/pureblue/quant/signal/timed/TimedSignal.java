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
package com.pureblue.quant.signal.timed;

import org.joda.time.DateTime;

/**
 * Updates a signal with new data and timestamps.
 *
 * @param <D> the type of signal data
 * @param <T> the output type
 * @author Haksun Li
 */
public interface TimedSignal<D, T> {

    /**
     * Update the signal with new data. This method does not return the signal value as computing it
     * may be expensive. We retrieve the signal value only when we need it by {@linkplain #value()}.
     * <p/>
     * The new arrival time should be after all previous times; otherwise, the behavior is not
     * defined. That is, no duplicated timestamps, no backward timestamps.
     *
     * @param t    the current time
     * @param data the current input
     */
    public void update(DateTime t, D data);

    /**
     * Gets the signal value.
     *
     * @return the signal value
     */
    public T value();
}
