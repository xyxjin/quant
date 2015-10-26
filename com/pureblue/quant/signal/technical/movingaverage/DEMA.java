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
package com.pureblue.quant.signal.technical.movingaverage;

import org.joda.time.DateTime;

import com.pureblue.quant.signal.timed.DoubleSignal;

/**
 * Double-exponential moving average.
 *
 * @author Haksun Li
 */
public class DEMA implements DoubleSignal<Double> {

    private final EMA ema1;
    private final EMA ema2;

    public DEMA(double alpha1, double alpha2) {
        this.ema1 = new EMA(alpha1);
        this.ema2 = new EMA(alpha2);
    }

    @Override
    public void update(DateTime t, Double x) {
        ema1.update(t, x);

        double v1 = ema1.value();
        ema2.update(t, v1);
    }

    @Override
    public Double value() {
        double v1 = ema1.value();
        double v2 = ema2.value();
        double value = 2 * v1 - v2; // 2 * EMA(x) - EMA(EMA(x))
        return value;
    }
}
