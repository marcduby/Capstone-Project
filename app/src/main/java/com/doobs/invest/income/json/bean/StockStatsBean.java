package com.doobs.invest.income.json.bean;

/**
 * Entity class to hold the financial information on a stock
 *
 * Created by mduby on 11/17/18.
 */

public class StockStatsBean {
    // instance variables
    private String symbol;
    private Double beta;
    private Double dividend;
    private Double yield;
    private Double revenuePerShare;
    private Double priceToSales;
    private Double priceToBook;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getBeta() {
        return beta;
    }

    public void setBeta(Double beta) {
        this.beta = beta;
    }

    public Double getDividend() {
        return dividend;
    }

    public void setDividend(Double dividend) {
        this.dividend = dividend;
    }

    public Double getYield() {
        return yield;
    }

    public void setYield(Double yield) {
        this.yield = yield;
    }

    public Double getRevenuePerShare() {
        return revenuePerShare;
    }

    public void setRevenuePerShare(Double revenuePerShare) {
        this.revenuePerShare = revenuePerShare;
    }

    public Double getPriceToSales() {
        return priceToSales;
    }

    public void setPriceToSales(Double priceToSales) {
        this.priceToSales = priceToSales;
    }

    public Double getPriceToBook() {
        return priceToBook;
    }

    public void setPriceToBook(Double priceToBook) {
        this.priceToBook = priceToBook;
    }
}
