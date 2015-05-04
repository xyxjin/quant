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
package util;

import org.joda.time.DateTimeConstants;

/**
 * Type-safe day-of-week constants (instead of integers).
 *
 * @author Ken Yiu
 */
public enum DayOfWeek {

    /**
     * Sunday.
     */
    SUNDAY(DateTimeConstants.SUNDAY),
    /**
     * Monday.
     */
    MONDAY(DateTimeConstants.MONDAY),
    /**
     * Tuesday.
     */
    TUESDAY(DateTimeConstants.TUESDAY),
    /**
     * Wednesday.
     */
    WEDNESDAY(DateTimeConstants.WEDNESDAY),
    /**
     * Thursday.
     */
    THURSDAY(DateTimeConstants.THURSDAY),
    /**
     * Friday.
     */
    FRIDAY(DateTimeConstants.FRIDAY),
    /**
     * Saturday.
     */
    SATURDAY(DateTimeConstants.SATURDAY);

    private final int jodaTimeValue;

    private DayOfWeek(int jodaTimeValue) {
        this.jodaTimeValue = jodaTimeValue;
    }

    /**
     * Returns the integer value used in joda-time API.
     *
     * @return the joda-time value
     */
    public int toJodaTimeValue() {
        return jodaTimeValue;
    }
}
