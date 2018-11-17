package com.doobs.invest.income.model;

import com.doobs.invest.income.util.IncomeConstants;

import java.util.Date;

/**
 * Entity class to hold the stock quote information
 *
 * Created by mduby on 11/16/18.
 */

public class StockQuoteBean {
    // local variables
    private StockBean stockBean;
    private float price;
    private Date date;
    private float dividend;

    public StockBean getStockBean() {
        return stockBean;
    }

    public void setStockBean(StockBean stockBean) {
        this.stockBean = stockBean;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getDividend() {
        return dividend;
    }

    public void setDividend(float dividend) {
        this.dividend = dividend;
    }
}
