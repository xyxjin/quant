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
package com.pureblue.quant.util.frequency;

import org.joda.time.DateTime;

/**
 * Specifies a way of partitioning time by period. For each given time <i>t</i> it gives the begin
 * time <i>t<sub>begin</sub></i> (inclusive) and the end time <i>t<sub>end</sub></i> (exclusive) of
 * the period. That is,
 * <blockquote>
 * t &isin; [t<sub>begin</sub>, t<sub>end</sub>)
 * </blockquote>
 *
 * @author Johannes Lehmann
 */
public interface Frequency {

    /**
     * Gets the begin time of the period that the time belongs to. The time returned must be less
     * than or equal to the argument.
     *
     * @param time a time
     * @return the period begin time
     */
    public DateTime periodBegin(DateTime time);

    /**
     * Gets the end time of the period that the time belongs to. The time must be greater than the
     * argument.
     *
     * @param time a time
     * @return the period end time
     */
    public DateTime periodEnd(DateTime time);
}
