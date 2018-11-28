package com.doobs.invest.income.util;

import android.graphics.Color;

import com.doobs.invest.income.model.StockHoldingModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static String getCurrencyString(Double value) {
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
    public static String getPercentString(Double value) {
        // local variables
        String formattedDouble = null;
        DecimalFormat formatter = new DecimalFormat("#.##");

        // if null, set as 0
        if (value == null) {
            value = 0.0;
        }

        // format the double
        formattedDouble = formatter.format(value) + " %";

        // return
        return formattedDouble;
    }

    /**
     * return the current date as a string
     *
     * @return
     */
    public static String getCurrentDateString() {
        // local variables
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String dateString = null;

        // format the data
        dateString = formatter.format(date);

        // return
        return dateString;
    }

    /**
     * return the industry break down of the stock holding list by total value
     *
     * @param stockHoldingModelList
     * @return
     */
    public static Map<String, Double> getIndustryMap(List<StockHoldingModel> stockHoldingModelList) {
        // local variables
        Map<String, Double> industryMap = new HashMap<String, Double>();
        String industry = null;
        Double amount = null;

        // loop through the list
        for (StockHoldingModel stockHoldingModel : stockHoldingModelList) {
            // get the industry
            industry = stockHoldingModel.getIndustry();

            // add if available
            if (industryMap.containsKey(industry)) {
                amount = industryMap.get(industry);
                industryMap.put(industry, amount + stockHoldingModel.getCurrentValue());

            } else {
                // create entry if not available
                industryMap.put(industry, stockHoldingModel.getCurrentValue());
            }
        }

        // return
        return industryMap;
    }

    /**
     * returns the colors to be used in the charts
     *
     * @return
     */
    public static ArrayList<Integer> getChartColors() {
        final int[] chartColors = {
                Color.rgb(0, 153, 255),        // blue
                Color.rgb(255, 0, 0),         // red
                Color.rgb(0, 255, 0),        // green
                Color.rgb(255, 153, 51),       // orange
                Color.rgb(255, 0, 255),       // purple
                Color.rgb(204, 204, 0)     // yellow
        };

        // create the array
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : chartColors) {
            colors.add(color);
        }

        // return
        return colors;
    }
}
