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
package signal.technical.volume;

import dao.OHLCPoint;
import signal.timed.DoubleSignal;

/**
 * This class does the shared calculations of the money flow volume indicators developed by Marc
 * Chaikin.
 *
 * @author Johannes Lehmann
 */
abstract class ChaikinVolumeSignal implements DoubleSignal<OHLCPoint> {

    private double moneyFlowMultiplier;
    private double moneyFlowVolume;

    // This signature intentionally doesn't match that of interface, to force subclasses to implement their own update.
    public Double update(OHLCPoint eod) {
        moneyFlowMultiplier = ((eod.getClose() - eod.getLow()) - (eod.getHigh() - eod.getClose())) / (eod.getHigh() - eod.getLow());
        moneyFlowVolume = moneyFlowMultiplier * eod.getVolume();
        return moneyFlowVolume;
    }

    public double moneyFlowMultiplier() {
        return moneyFlowMultiplier;
    }

    public double moneyFlowVolume() {
        return moneyFlowVolume;
    }
}
