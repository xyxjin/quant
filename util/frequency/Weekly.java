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

import util.DayOfWeek;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

/**
 * A weekly frequency, where the week can be defined to start on any day and at any time that day.
 *
 * @author Johannes Lehmann
 */
public class Weekly implements Frequency {

    private final DayOfWeek day;
    private final LocalTime dayStartTime;

    /**
     * Constructs a weekly frequency where the day on which the week starts is defined by a starting
     * day.
     *
     * @param day  the day on which the week starts
     * @param time the time at which a day is defined to start
     */
    public Weekly(DayOfWeek day, LocalTime time) {
        this.day = day;
        this.dayStartTime = time;
    }

    /**
     * Constructs a weekly frequency where the week starts on at midnight on Monday.
     */
    public Weekly() {
        this(DayOfWeek.MONDAY, LocalTime.MIDNIGHT);
    }

    /**
     * Gets the day where a week starts.
     *
     * @return the day where a week starts
     */
    public DayOfWeek firstDay() {
        return day;
    }

    /**
     * Gets the time where a day starts.
     *
     * @return the time where a day starts
     */
    public LocalTime firstTime() {
        return dayStartTime;
    }

    @Override
    public DateTime periodBegin(DateTime time) {
        int timeDifference = time.getDayOfWeek() - day.toJodaTimeValue();
        if (timeDifference < 0) {
            timeDifference += 7;
        }
        DateTime startDay = time.minusDays(timeDifference);
        DateTime weekStart = new DateTime(startDay.getYear(),
                                          startDay.getMonthOfYear(),
                                          startDay.getDayOfMonth(),
                                          dayStartTime.getHourOfDay(),
                                          dayStartTime.getMinuteOfHour(),
                                          dayStartTime.getSecondOfMinute(),
                                          dayStartTime.getMillisOfSecond(),
                                          startDay.getZone());
        if (weekStart.isAfter(time)) {
            return weekStart.minusWeeks(1);
        } else {
            return weekStart;
        }
    }

    @Override
    public DateTime periodEnd(DateTime time) {
        return periodBegin(time).plusWeeks(1);
    }
}
