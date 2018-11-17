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
    private Double price;
    private Date date;
    private Double dividend;

    public StockBean getStockBean() {
        return stockBean;
    }

    public void setStockBean(StockBean stockBean) {
        this.stockBean = stockBean;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDividend() {
        return dividend;
    }

    public void setDividend(Double dividend) {
        this.dividend = dividend;
    }
}
