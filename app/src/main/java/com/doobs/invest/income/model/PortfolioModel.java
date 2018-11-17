package com.doobs.invest.income.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class to hold stock portfolio information
 *
 * Created by mduby on 11/16/18.
 */

public class PortfolioModel {
    // local variables
    private String name;
    private String descriprion;
    private String goal;
    private List<StockHoldingModel> stockHoldingList = new ArrayList<StockHoldingModel>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriprion() {
        return descriprion;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public List<StockHoldingModel> getStockHoldingList() {
        return stockHoldingList;
    }

    public void setStockHoldingList(List<StockHoldingModel> stockHoldingList) {
        this.stockHoldingList = stockHoldingList;
    }
}
