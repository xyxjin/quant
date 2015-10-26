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
import org.joda.time.Period;

/**
 * Allows defining a frequency where consecutive periods are offset by a fixed period and the first
 * period starts at a particular time.
 *
 * @author Johannes Lehmann
 */
public class CustomFrequency implements Frequency {

    private final DateTime startTime;
    private final Period duration; // the duration or length of one period

    /**
     * Constructs a new instance, where the first period starts at {@code startDate} and consecutive
     * periods are offset by period length.
     *
     * @param startDate the start of the first period
     * @param duration  the duration or length of one period
     */
    public CustomFrequency(DateTime startDate, Period duration) {
        this.startTime = startDate;
        this.duration = duration;
    }

    @Override
    public DateTime periodBegin(DateTime time) { // TODO: improve efficiency by removing the while loops
        DateTime currentTime = startTime;
        if (time.isAfter(startTime)) {
            while (!currentTime.isAfter(time)) {
                currentTime = currentTime.plus(duration);
            }
            return currentTime.minus(duration);
        } else {
            while (currentTime.isAfter(time)) {
                currentTime = currentTime.minus(duration);
            }
            return currentTime;
        }
    }

    @Override
    public DateTime periodEnd(DateTime time) {
        return periodBegin(time).plus(duration);
    }
}
