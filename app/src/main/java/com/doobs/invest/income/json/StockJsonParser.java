package com.doobs.invest.income.json;

import com.doobs.invest.income.json.bean.StockInformationBean;
import com.doobs.invest.income.json.bean.StockQuoteBean;
import com.doobs.invest.income.json.bean.StockStatsBean;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Json parrsign class to parse the json results of REST calls
 *
 * Created by mduby on 11/15/18.
 */

public class StockJsonParser {
    // instance variables

    /**
     * parse the json string input into a stock bean
     *
     * @param inputJsonString
     * @return
     * @throws IncomeException
     */
    public static StockInformationBean getStockInformationFromJsonString(String inputJsonString) throws IncomeException {
        // local variables
        StockInformationBean stockInformationBean = null;
        JSONObject jsonObject = null;

        // get the json object
        if (inputJsonString == null) {
            throw new IncomeException("Got null input json to translate to stockInformationBean object");

        } else {
            try {
                jsonObject = new JSONObject(inputJsonString);

            } catch (JSONException exception) {
                throw new IncomeException("Got json exception translating to stockInformationBean object: " + exception.getMessage());
            }
        }

        // get the list
        stockInformationBean = getStockInformationFromJson(jsonObject);

        // return
        return stockInformationBean;
    }

    /**
     * parse the json input into a stock bean
     *
     * @param inputJsonObject
     * @return
     * @throws IncomeException
     */
    public static StockInformationBean getStockInformationFromJson(JSONObject inputJsonObject) throws IncomeException {
        // local variables
        StockInformationBean stockInformationBean = new StockInformationBean();
        String tempString = null;

        // get the symbol
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.SYMBOL_KEY);
        stockInformationBean.setSymbol(tempString);

        // get the name
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.NAME_KEY);
        stockInformationBean.setName(tempString);

        // get the description
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.DESCRIPTION_KEY);
        stockInformationBean.setDescription(tempString);

        // get the industry
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.INDUSTRY_KEY);
        stockInformationBean.setIndustry(tempString);

        // get the issue type
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.ISSUE_TYPE_KEY);
        stockInformationBean.setIssueType(tempString);

        // verify the data
        if ((stockInformationBean.getSymbol() == null) && (stockInformationBean.getSymbol().length() > 0)) {
            throw new IncomeException("A stock must have a non null symbol");
        }
        if ((stockInformationBean.getName() == null) && (stockInformationBean.getName().length() > 0)) {
            throw new IncomeException("A stock must have a non null name");
        }

        // return
        return stockInformationBean;
    }

    /**
     * returns the total yearly dividend from the json string
     *
     * @param inputJsonString
     * @return
     * @throws IncomeException
     */
    public static Double getYearlyDividendFromJsonString(String inputJsonString) throws IncomeException {
        // local variables
        Double dividend = null;
        JSONArray jsonArray = null;

        // get the json object
        if (inputJsonString == null) {
            throw new IncomeException("Got null input json to translate to dividend object");

        } else {
            try {
                jsonArray = new JSONArray(inputJsonString);

            } catch (JSONException exception) {
                throw new IncomeException("Got json exception translating to dividend object: " + exception.getMessage());
            }
        }

        // get the list
        dividend = getDividendFromJson(jsonArray);

        // return
        return dividend;
    }

    /**
     * returns the total dividend from the json array
     *
     * @param jsonArray
     * @return
     * @throws IncomeException
     */
    public static Double getDividendFromJson(JSONArray jsonArray) throws IncomeException {
        //local variables
        Double dividend = null;
        Double totalDividend = 0.0;
        JSONObject jsonObject = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.optJSONObject(i);

            // get the dividend amount
            try {
                dividend = jsonObject.getDouble(IncomeConstants.JsonKeys.Dividend.AMOUNT_KEY);
                totalDividend = totalDividend + dividend;

            } catch (JSONException exception) {
                throw new IncomeException("Got json exception parsing the dividend: " + exception.getMessage());
            }
        }

        // return
        return totalDividend;
    }

    /**
     * get the stock quote from the json string
     *
     * @param inputJsonString
     * @return
     * @throws IncomeException
     */
    public static StockQuoteBean getStockQuoteFromJsonString(String inputJsonString) throws IncomeException {
        // local variables
        StockQuoteBean stockQuoteBean = null;
        JSONObject jsonObject = null;

        // get the stock quote bean
        // get the json object
        if (inputJsonString == null) {
            throw new IncomeException("Got null input json to translate to stock quote object");

        } else {
            try {
                jsonObject = new JSONObject(inputJsonString);

            } catch (JSONException exception) {
                throw new IncomeException("Got json exception translating to stock quote object: " + exception.getMessage());
            }
        }

        // get the list
        stockQuoteBean = getStockQuoteFromJson(jsonObject);

        // return
        return stockQuoteBean;
    }

    /**
     * get the stock quote from the json object
     *
     * @param inputJsonObject
     * @return
     * @throws IncomeException
     */
    public static StockQuoteBean getStockQuoteFromJson(JSONObject inputJsonObject) throws IncomeException {
        // local variables
        StockQuoteBean stockQuoteBean = new StockQuoteBean();
        String tempString = null;
        Double tempDouble = null;
        Date tempDate = null;
        DateFormat formatter = new SimpleDateFormat("MMMMM dd, yyyy");

        // get the symbol
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Quote.SYMBOL_KEY);
        stockQuoteBean.setSymbol(tempString);

        // get the date
//        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Quote.DATE_KEY);
//        try {
//            tempDate = formatter.parse(tempString);
//
//        } catch (ParseException exception) {
//            throw new IncomeException("Got stock quote error parsing the date: " + exception.getMessage());
//        }
//        stockQuoteBean.setDate(tempDate);

        // get the price
        tempDouble = inputJsonObject.optDouble(IncomeConstants.JsonKeys.Quote.PRICE_KEY);
        stockQuoteBean.setPrice(tempDouble);

        // get the price change
        tempDouble = inputJsonObject.optDouble(IncomeConstants.JsonKeys.Quote.PRICE_CHANGE_KEY);
        stockQuoteBean.setPriceChange(tempDouble);

        // get the PE ratio
        tempDouble = inputJsonObject.optDouble(IncomeConstants.JsonKeys.Quote.PE_RATIO_KEY);
        stockQuoteBean.setPeRatio(tempDouble);

        // return
        return stockQuoteBean;
    }

    /**
     * get the stock financial statistics fromt he json string
     *
     * @param inputJsonString
     * @return
     * @throws IncomeException
     */
    public static StockStatsBean getStockStatsFromJsonString(String inputJsonString) throws IncomeException {
        // local variables
        StockStatsBean stockStatsBean = null;
        JSONObject jsonObject = null;

        // get the stock quote bean
        // get the json object
        if (inputJsonString == null) {
            throw new IncomeException("Got null input json to translate to stock stats object");

        } else {
            try {
                jsonObject = new JSONObject(inputJsonString);

            } catch (JSONException exception) {
                throw new IncomeException("Got json exception translating to stock stats object: " + exception.getMessage());
            }
        }

        // get the list
        stockStatsBean = getStockStatsFromJson(jsonObject);

        // return
        return stockStatsBean;
    }

    /**
     * get the stock financial information from the json object
     *
     * @param inputJsonObject
     * @return
     * @throws IncomeException
     */
    public static StockStatsBean getStockStatsFromJson(JSONObject inputJsonObject) throws IncomeException {
        // local variables
        StockStatsBean stockStatsBean = new StockStatsBean();
        String tempString = null;
        Double tempDouble = null;

        // get the symbol
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Financials.SYMBOL_KEY);
        stockStatsBean.setSymbol(tempString);

        // get the beta
        tempDouble = inputJsonObject.optDouble(IncomeConstants.JsonKeys.Financials.BETA_KEY);
        stockStatsBean.setBeta(tempDouble);

        // get the dividend
        tempDouble = inputJsonObject.optDouble(IncomeConstants.JsonKeys.Financials.DIVIDEND_KEY);
        stockStatsBean.setDividend(tempDouble);

        // get the yield
        tempDouble = inputJsonObject.optDouble(IncomeConstants.JsonKeys.Financials.YIELD_KEY);
        stockStatsBean.setYield(tempDouble);

        // get the price to book
//        tempDouble = inputJsonObject.optDouble(IncomeConstants.JsonKeys.Financials.PRICE_TO_BOOK_KEY);
//        stockStatsBean.setPriceToBook(tempDouble);
//
//        // get the price to sales
//        tempDouble = inputJsonObject.optDouble(IncomeConstants.JsonKeys.Financials.PRICE_TO_SALES_KEY);
//        stockStatsBean.setPriceToSales(tempDouble);
//
//        // get the revenue per share
//        tempDouble = inputJsonObject.optDouble(IncomeConstants.JsonKeys.Financials.REVENUE_PER_HARE_KEY);
//        stockStatsBean.setRevenuePerShare(tempDouble);

        // return
        return stockStatsBean;
    }
}