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
package com.pureblue.quant.signal.technical;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import com.pureblue.quant.signal.timed.TimedSignal;
import com.pureblue.quant.util.MovingWindowBySize;

/**
 * Average Daily Movement is the moving average of movements defined as (daily high - daily low).
 * It is a volatility measure. The value of this signal is NaN during the first day of data.
 *
 * @author Johannes Lehmann
 */
public class AverageDailyMovement implements TimedSignal<Double, Double> {

    private final LocalTime oneDaySpan;
    private final MovingWindowBySize<Double> window;
    private DateTime currentDay = null;
    private double dailyHigh = Double.NEGATIVE_INFINITY;
    private double dailyLow = Double.POSITIVE_INFINITY;

    /**
     * Constructs an instance to track the average daily movement over a fixed length window.
     *
     * @param dayStartTime the time at which a day starts (inclusive)
     * @param size         the size of the moving window
     */
    public AverageDailyMovement(LocalTime dayStartTime, int size) {
        this.oneDaySpan = dayStartTime;
        this.window = new MovingWindowBySize<>(size);
    }

    /**
     * Constructs an instance to track the average daily movement over a fixed length window, with
     * the start time of a day defined as midnight (that is, 00:00).
     *
     * @param size the size of the moving window
     */
    public AverageDailyMovement(int size) {
        this(new LocalTime(LocalTime.MIDNIGHT), size);
    }

    @Override
    public void update(DateTime t, Double px) {
        if (currentDay == null) {
            currentDay = t; // first run
            dailyHigh = px;
            dailyLow = px;
        }

        //if (!oneDaySpan.isSameDay(t, currentDay)) {
        if (currentDay.isEqual(t)) {
            double range = dailyHigh - dailyLow; // range of yesterday
            window.add(range);

            currentDay = t; // a new day
            dailyHigh = px;
            dailyLow = px;
        } else {
            if (px > dailyHigh) {
                dailyHigh = px;
            }
            if (px < dailyLow) {
                dailyLow = px;
            }
        }
    }

    @Override
    public Double value() {
        if (window.size() == 0) {
            return Double.NaN;
        }

        //double value = new Mean(window.asList()).value();
        @SuppressWarnings("unchecked")
        double value = ((TimedSignal<Double, Double>) new Mean()).value();
        return value;
    }

}
