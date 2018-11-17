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
    private Double lastPrice;
    private Double lastYearlyDividend;
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

    public Date getLastPriceDate() {
        return lastPriceDate;
    }

    public void setLastPriceDate(Date lastPriceDate) {
        this.lastPriceDate = lastPriceDate;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getLastYearlyDividend() {
        return lastYearlyDividend;
    }

    public void setLastYearlyDividend(Double lastYearlyDividend) {
        this.lastYearlyDividend = lastYearlyDividend;
    }
}
