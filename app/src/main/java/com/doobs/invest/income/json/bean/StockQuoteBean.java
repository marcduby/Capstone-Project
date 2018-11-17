package com.doobs.invest.income.json.bean;

import com.doobs.invest.income.json.bean.StockInformationBean;

import java.util.Date;

/**
 * Entity class to hold the stock quote information
 *
 * Created by mduby on 11/16/18.
 */

public class StockQuoteBean {
    // local variables
    private String symbol;
    private Double price;
    private Double priceChange;
    private Double peRatio;
    private Date date;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Double priceChange) {
        this.priceChange = priceChange;
    }

    public Double getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(Double peRatio) {
        this.peRatio = peRatio;
    }
}
