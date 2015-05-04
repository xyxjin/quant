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
import org.joda.time.DateTime;

/**
 * Accumulation Distribution Line measures cumulative flow of money in and out of a security.
 *
 * @author Johannes Lehmann
 * @see <a
 * href="http://en.wikipedia.org/wiki/Accumulation/distribution_index">Accumulation/Distribution
 * Index</a>
 */
public class AccumulationDistributionLine extends ChaikinVolumeSignal {

    private double value = 0.0;

    @Override
    public void update(DateTime t, OHLCPoint data) {
        super.update(data);
        value += moneyFlowVolume();
    }

    @Override
    public Double value() {
        return value;
    }
}
