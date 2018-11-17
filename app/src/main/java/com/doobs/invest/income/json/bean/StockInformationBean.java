package com.doobs.invest.income.json.bean;

import java.util.Date;

/**
 * Entity class to hold the stock information
 *
 * Created by mduby on 11/15/18.
 */

public class StockInformationBean {
    // instance variables
    private String symbol;
    private String name;
    private String industry;
    private String issueType;
    private String description;

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

}
