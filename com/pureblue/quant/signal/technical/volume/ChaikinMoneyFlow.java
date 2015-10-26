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
package com.pureblue.quant.signal.technical.volume;

import org.joda.time.DateTime;
import org.apache.commons.math3.stat.*;

import com.pureblue.quant.dao.OHLCPoint;
import com.pureblue.quant.util.DoubleUtils;
import com.pureblue.quant.util.MovingWindowBySize;

/**
 * The Chaikin Money Flow is similar to the Accumulation Distribution Line, but instead of measuring
 * the cumulative flow of money, it measures the flow of money over a specified period of time.
 *
 * @author Johannes Lehmann
 * @see <a
 * href="http://en.wikipedia.org/wiki/Chaikin_Stock_Research#Chaikin_Money_Flow">Chaikin Money
 * Flow</a>
 */


public class ChaikinMoneyFlow extends ChaikinVolumeSignal {

    private final MovingWindowBySize<Double> moneyFlowVolumes;
    private final MovingWindowBySize<Long> volumes;

    public ChaikinMoneyFlow(int lag) {
        this.moneyFlowVolumes = new MovingWindowBySize<>(lag);
        this.volumes = new MovingWindowBySize<>(lag);
    }

    @Override
    public void update(DateTime t, OHLCPoint eod) {
        super.update(eod);
        moneyFlowVolumes.add(moneyFlowVolume());
        volumes.add(eod.getVolume());
    }

    @Override
    public Double value() {
        if (!moneyFlowVolumes.isReady() || !volumes.isReady()) {
            return Double.NaN;
        }
        
        double moneyFlowVolumeSum = StatUtils.sum(DoubleUtils.collection2DoubleArray(moneyFlowVolumes.asList()));
        double volumeSum = StatUtils.sum(DoubleUtils.Longcollection2DoubleArray(volumes.asList()));
        double value = moneyFlowVolumeSum / volumeSum;
        return value;
    }
}
