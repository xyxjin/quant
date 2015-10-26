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

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;

/**
 * This is a composite signal computed as a weighted sum of other signals.
 *
 * @param <D> the type of signal data
 * @author Ken Yiu
 */
public class WeightedSumSignal<D> implements DoubleSignal<D[]> {

    public static class WeightedSignal<D> implements DoubleSignal<D> {

        private final DoubleSignal<D> signal;
        private final double weight;

        public WeightedSignal(DoubleSignal<D> signal, double weight) {
            this.signal = signal;
            this.weight = weight;
        }

        @Override
        public Double value() {
            return signal.value() * weight;
        }

        @Override
        public void update(DateTime t, D data) {
            signal.update(t, data);
        }
    }

    private final List<WeightedSignal<D>> signals;
    private double value;

    /**
     * Constructs an instance from {@linkplain WeightedSignal}s.
     *
     * @param signals the weighted signals
     */
    @SafeVarargs
    public WeightedSumSignal(WeightedSignal<D>... signals) {
        this.signals = Arrays.asList(signals);
    }

    /**
     * Updates the component signals and then the weighted sum of the signals.
     *
     * @param t    the time
     * @param data the data
     */
    @Override
    public void update(DateTime t, D[] data) {
        double sum = 0;

        for (int i = 0; i < signals.size(); ++i) {
            WeightedSignal<D> signal = signals.get(i);
            signal.update(t, data[i]);
            sum += signal.value();
        }

        value = sum; // update the value
    }

    @Override
    public Double value() {
        return value;
    }
}
