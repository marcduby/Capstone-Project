package com.doobs.invest.income.util;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.awt.font.TextAttribute;
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
    // constants
    public static String TAG_NAME = IncomeUtils.class.getName();

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
                Color.rgb(153, 102, 51),       // brown
                Color.rgb(128, 128, 128),       // grey
                Color.rgb(102, 153, 153),       // teal
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

    /**
     * update the portfolio totals based on the stock holdings given
     *
     * @param stockHoldingModelList
     * @param portfolioModel
     */
    public static void updatePortfolioTotals(List<StockHoldingModel> stockHoldingModelList, PortfolioModel portfolioModel) {
        // locall variables
        Double cost = new Double(0);
        Double value = new Double(0);
        Double dividend = new Double(0);

        // log
        Log.i(TAG_NAME, "Updating totals for portfolio id: " + portfolioModel.getId() + " and name: " + portfolioModel.getName());

        // add up the amounts
        for (StockHoldingModel stockHoldingModel : stockHoldingModelList) {
            cost = cost + stockHoldingModel.getCostBasis();
            value = value + stockHoldingModel.getCurrentValue();
            dividend = dividend + stockHoldingModel.getTotalDividend();
        }
        portfolioModel.setCostBasis(cost);
        portfolioModel.setCurrentValue(value);
        portfolioModel.setTotalDividend(dividend);
    }

    /**
     * log a firebase event
     *
     * @param eventType
     */
    public static void logFirebaseEvent(FirebaseAnalytics firebaseAnalytics, String eventType) {
        // create the bundle
        Bundle bundle = new Bundle();

        // log the event
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, eventType);

        // send
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
