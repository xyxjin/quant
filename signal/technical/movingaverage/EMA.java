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
package signal.technical.movingaverage;

import signal.timed.DoubleSignal;
import org.joda.time.DateTime;

/**
 * Exponential moving average.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Moving_average#Exponential_moving_average"> Wikipedia:
 * Exponential moving average</a>
 */
public class EMA implements DoubleSignal<Double> {

    private final double alpha;
    private double value = Double.NaN;

    /**
     * Constructs an EMA instance.
     *
     * @param alpha decaying factor
     */
    public EMA(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Constructs an EMA instance with a default alpha.
     * <blockquote><pre>
     * &alpha; = 2/(N+1)
     * </pre></blockquote>
     *
     * @param N period
     */
    public EMA(int N) {
        this(2. / (N + 1.));
    }

    @Override
    public void update(DateTime t, Double x) {
        value = Double.isNaN(value) ? x // initialization
                : alpha * x + (1. - alpha) * value; // EMA definition
    }

    @Override
    public Double value() {
        return value;
    }
}
