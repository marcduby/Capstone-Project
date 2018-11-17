package com.doobs.invest.income.model;

import java.util.Date;

/**
 * Entity class to hold the stock position information
 *
 * Created by mduby on 11/15/18.
 */

public class StockHoldingBean {
    // local variables
    private float numberOfShares;
    private float lastPrice;
    private PortfolioBean portfolioBean;
    private StockBean stockBean;

    public float getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(float numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public float getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(float lastPrice) {
        this.lastPrice = lastPrice;
    }

    public PortfolioBean getPortfolioBean() {
        return portfolioBean;
    }

    public void setPortfolioBean(PortfolioBean portfolioBean) {
        this.portfolioBean = portfolioBean;
    }

    public StockBean getStockBean() {
        return stockBean;
    }

    public void setStockBean(StockBean stockBean) {
        this.stockBean = stockBean;
    }
}
