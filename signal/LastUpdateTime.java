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
package signal;

import org.joda.time.DateTime;

/**
 * Tracks the last update time, say, of a signal. For example, the same signal instance may be
 * called multiple times by multiple enclosing object with the same data. We want to avoid doing the
 * same update over and over again.
 *
 * @author Haksun Li
 */

public class LastUpdateTime {

    private DateTime t = new DateTime(2003,1,1,0,0); // last update time

    public boolean isPast(DateTime now) { // The past is in the past! Let it go! Let it go!
        if (!now.isAfter(t)) { // now <= t
            return true;
        }

        // now after t
        t = now;
        return false;
    }

    public DateTime timestamp() {
        return t;
    }
}
