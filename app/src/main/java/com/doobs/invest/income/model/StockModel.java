package com.doobs.invest.income.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.doobs.invest.income.util.IncomeConstants;

import java.util.Date;

/**
 * Model class to hold the latest stock financial and company information
 *
 * Created by mduby on 11/17/18.
 */
@Entity(tableName = IncomeConstants.Database.TABLE_NAME_HOLDING)
public class StockModel {
    // local variables
    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey
    private Integer id;

    // daily price information
    @ColumnInfo(name = "symbol")
    private String symbol;

    @ColumnInfo(name = "price")
    private Double price;

    @ColumnInfo(name = "price_change")
    private Double priceChange;

    @ColumnInfo(name = "pe_ratio")
    private Double peRatio;

    @ColumnInfo(name = "date")
    private Date date;

    // company information
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "industry")
    private String industry;

    @ColumnInfo(name = "issue_type")
    private String issueType;

    @ColumnInfo(name = "description")
    private String description;

    // financial information
    @ColumnInfo(name = "beta")
    private Double beta;

    @ColumnInfo(name = "dividend")
    private Double didvidend;

    @ColumnInfo(name = "yield")
    private Double yield;

    @ColumnInfo(name = "revenue_per_share")
    private Double revenuePerShare;

    @ColumnInfo(name = "price_to_sales")
    private Double priceToSales;

    @ColumnInfo(name = "price_to_book")
    private Double priceToBook;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getBeta() {
        return beta;
    }

    public void setBeta(Double beta) {
        this.beta = beta;
    }

    public Double getDidvidend() {
        return didvidend;
    }

    public void setDidvidend(Double didvidend) {
        this.didvidend = didvidend;
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

    @NonNull
    public Integer getId() {
        return id;
    }
}
