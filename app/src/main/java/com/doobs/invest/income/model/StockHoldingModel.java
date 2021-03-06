package com.doobs.invest.income.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.doobs.invest.income.json.bean.StockInformationBean;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeException;

/**
 * Entity class to hold the stock position information
 *
 * Created by mduby on 11/15/18.
 */
@Entity(tableName = IncomeConstants.Database.TABLE_NAME_HOLDING)
public class StockHoldingModel {
    // local variables
    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @NonNull
    @ColumnInfo(name = "number_shares")
    private Double numberOfShares;

    @NonNull
    @ColumnInfo(name = "price_paid")
    private Double pricePaid;

    @NonNull
    @ColumnInfo(name = "stock_id")
    private Integer stockId;

    @NonNull
    @ColumnInfo(name = "portfolio_id")
    private Integer portfolioId;

    @NonNull
    @ColumnInfo(name = "symbol")
    private String stockSymbol;

    @NonNull
    @ColumnInfo(name = "industry")
    private String industry;

    @ColumnInfo(name = "current_value")
    private Double currentValue;

    @ColumnInfo(name = "total_dividend")
    private Double totalDividend;

//    @Ignore
//    private StockModel stockModel;

    public Double getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(Double numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

//    public StockModel getStockModel() {
//        return stockModel;
//    }
//
//    public void setStockModel(StockModel stockModel) {
//        this.stockModel = stockModel;
//    }

    @NonNull
    public Integer getId() {
        return id;
    }

    @NonNull
    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(@NonNull Integer stockId) {
        this.stockId = stockId;
    }

    @NonNull
    public Integer getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(@NonNull Integer portfolioId) {
        this.portfolioId = portfolioId;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public Double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(@NonNull Double pricePaid) {
        this.pricePaid = pricePaid;
    }

    /**
     * get the cost basis of the stock holding
     *
     * @return
     */
    public Double getCostBasis() {
        return this.numberOfShares * this.pricePaid;
    }

//    /**
//     * get the current value basis of the stock holding
//     *
//     * @return
//     */
//    public Double getValueBasis() {
//        return this.numberOfShares * this.stockModel.getPrice();
//    }
//
//    /**
//     * get the total dividedn expected from the stock holding
//     *
//     * @return
//     */
//    public Double getYearlyDivided() {
//        return this.numberOfShares * this.stockModel.getDividend();
//    }

    /**
     * return a description of the stock holding
     *
     * @return
     */
    public String getDescription() {
        return this.numberOfShares.toString() + " shares of " + this.stockSymbol;
    }

    @NonNull
    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(@NonNull String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public Double getTotalDividend() {
        return totalDividend;
    }

    public void setTotalDividend(Double totalDividend) {
        this.totalDividend = totalDividend;
    }

    @NonNull
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(@NonNull String industry) {
        this.industry = industry;
    }

    /**
     * verify the stock holding data validity
     *
     * @throws IncomeException
     */
    public void validityCheck() throws IncomeException {
        // make sure stock id not null and not empty
        if (this.stockId == null) {
            throw new IncomeException("The stock holding stock cannot be empty");
        }

        // make sure portfolio id not null and not empty
        if (this.portfolioId == null) {
            throw new IncomeException("The stock holding portfolio cannot be empty");
        }

        // make sure number of shares not null and not empty
        if ((this.numberOfShares == null) || (this.numberOfShares <= 0)) {
            throw new IncomeException("The number of shares cannot be empty or less than or equal to 0");
        }

        // make sure price bought not null and not empty
        if ((this.pricePaid == null) || (this.pricePaid < 0)) {
            throw new IncomeException("The price bought cannot be empty or less than 0");
        }
    }

}
