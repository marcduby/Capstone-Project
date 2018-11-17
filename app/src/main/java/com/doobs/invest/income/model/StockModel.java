package com.doobs.invest.income.model;

import java.util.Date;

/**
 * Model class to hold the latest stock financial and company information
 *
 * Created by mduby on 11/17/18.
 */

public class StockModel {
    // local variables
    // daily price information
    private String symbol;
    private Double price;
    private Double priceChange;
    private Double peRatio;
    private Date date;

    // company information
    private String name;
    private String industry;
    private String issueType;
    private String description;

    // financial information
    private Double beta;
    private Double didvidend;
    private Double yield;
    private Double revenuePerShare;
    private Double priceToSales;
    private Double priceToBook;


}
