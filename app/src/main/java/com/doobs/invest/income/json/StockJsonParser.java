package com.doobs.invest.income.json;

import com.doobs.invest.income.model.StockBean;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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
    public static StockBean parseString(String inputJsonString) throws IncomeException {
        // local variables
        StockBean stockBean = null;
        JSONObject jsonObject = null;

        // get the json object
        if (inputJsonString == null) {
            throw new IncomeException("Got null input json to translate to stockBean object");

        } else {
            try {
                jsonObject = new JSONObject(inputJsonString);

            } catch (JSONException exception) {
                throw new IncomeException("Got json exception translating to stockBean object: " + exception.getMessage());
            }
        }

        // get the list
        stockBean = getStocktFromJson(jsonObject);

        // return
        return stockBean;
    }

    /**
     * parse the json input into a stock bean
     *
     * @param inputJsonObject
     * @return
     * @throws IncomeException
     */
    public static StockBean getStocktFromJson(JSONObject inputJsonObject) throws IncomeException {
        // local variables
        StockBean stockBean = new StockBean();
        String tempString = null;

        // get the symbol
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.SYMBOL_KEY);
        stockBean.setSymbol(tempString);

        // get the name
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.NAME_KEY);
        stockBean.setName(tempString);

        // get the description
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.DESCRIPTION_KEY);
        stockBean.setDescription(tempString);

        // get the industry
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.INDUSTRY_KEY);
        stockBean.setIndustry(tempString);

        // get the issue type
        tempString = inputJsonObject.optString(IncomeConstants.JsonKeys.Stock.ISSUE_TYPE_KEY);
        stockBean.setIssueType(tempString);

        // verify the data
        if ((stockBean.getSymbol() == null) && (stockBean.getSymbol().length() > 0)) {
            throw new IncomeException("A stock must have a non null symbol");
        }
        if ((stockBean.getName() == null) && (stockBean.getName().length() > 0)) {
            throw new IncomeException("A stock must have a non null name");
        }

        // return
        return stockBean;
    }

}
