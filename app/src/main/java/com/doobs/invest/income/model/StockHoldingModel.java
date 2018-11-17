package com.doobs.invest.income.model;

import com.doobs.invest.income.json.bean.StockInformationBean;

/**
 * Entity class to hold the stock position information
 *
 * Created by mduby on 11/15/18.
 */

public class StockHoldingModel {
    // local variables
    private Double numberOfShares;
    private StockModel stockModel;

    public Double getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(Double numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public StockModel getStockModel() {
        return stockModel;
    }

    public void setStockModel(StockModel stockModel) {
        this.stockModel = stockModel;
    }
}
