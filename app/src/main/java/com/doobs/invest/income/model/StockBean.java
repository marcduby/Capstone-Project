package com.doobs.invest.income.model;

import java.util.Date;

/**
 * Entity class to hold the stock information
 *
 * Created by mduby on 11/15/18.
 */

public class StockBean {
    // instance variables
    private String name;
    private String symbol;
    private String industry;
    private String issueType;
    private String description;
    private float lastPrice;
    private float lastYearlyDividend;
    private Date lastPriceDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(float lastPrice) {
        this.lastPrice = lastPrice;
    }

    public float getLastYearlyDividend() {
        return lastYearlyDividend;
    }

    public void setLastYearlyDividend(float lastYearlyDividend) {
        this.lastYearlyDividend = lastYearlyDividend;
    }

    public Date getLastPriceDate() {
        return lastPriceDate;
    }

    public void setLastPriceDate(Date lastPriceDate) {
        this.lastPriceDate = lastPriceDate;
    }
}
