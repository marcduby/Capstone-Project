package com.doobs.invest.income.util;

import java.text.NumberFormat;

/**
 * Utility class to provide various shared utility methods
 *
 * Created by mduby on 11/26/18.
 */

public class IncomeUtils {

    /**
     * returns a currency format for the double
     *
     * @param value
     * @return
     * @throws IncomeException
     */
    public static String getCurrencyString(Double value) throws IncomeException {
        // local variables
        String formattedDouble = null;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        // if null, set as 0
        if (value == null) {
            value = 0.0;
        }

        // format the double
        formattedDouble = formatter.format(value);

        // return
        return formattedDouble;
    }

    /**
     * returns the percent format for the double
     *
     * @param value
     * @return
     * @throws IncomeException
     */
    public static String getPercentString(Double value) throws IncomeException {
        // local variables
        String formattedDouble = null;
        NumberFormat formatter = NumberFormat.getPercentInstance();

        // if null, set as 0
        if (value == null) {
            value = 0.0;
        }

        // format the double
        formattedDouble = formatter.format(value);

        // return
        return formattedDouble;
    }
}