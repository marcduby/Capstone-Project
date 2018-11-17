package com.doobs.invest.income.model;

import java.util.Date;

/**
 * Entity class to hold the stock position information
 *
 * Created by mduby on 11/15/18.
 */

public class StockHoldingBean {
    // local variables
    private Double numberOfShares;
    private Double lastPrice;
    private PortfolioBean portfolioBean;
    private StockBean stockBean;

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

    public Double getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(Double numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }
}
