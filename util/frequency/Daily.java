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
package util.frequency;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

/**
 * A daily frequency, starting at the defined time of the time zone defined by time and ending at
 * the same time 24 hours later.
 *
 * @author Johannes Lehmann
 */
public class Daily implements Frequency {

    private final LocalTime time;

    /**
     * Constructs a new instance where the day starts at the given start time.
     *
     * @param time the time at which a day starts
     */
    public Daily(LocalTime time) {
        this.time = time;
    }

    /**
     * Constructs a new instance where a day is defined to start at midnight.
     */
    public Daily() {
        this(LocalTime.MIDNIGHT);
    }

    @Override
    public DateTime periodBegin(DateTime t) {
        DateTime currentDayStart = new DateTime(t.getYear(),
                                                t.getMonthOfYear(),
                                                t.getDayOfMonth(),
                                                time.getHourOfDay(),
                                                time.getMinuteOfHour(),
                                                time.getSecondOfMinute(),
                                                time.getMillisOfSecond(),
                                                t.getZone());
        if (currentDayStart.isAfter(t)) {
            return currentDayStart.minusDays(1);
        } else {
            return currentDayStart;
        }
    }

    @Override
    public DateTime periodEnd(DateTime time) {
        return periodBegin(time).plusDays(1);
    }
}
