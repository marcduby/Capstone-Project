package com.doobs.invest.income.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.doobs.invest.income.json.bean.StockInformationBean;
import com.doobs.invest.income.util.IncomeConstants;

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
    @PrimaryKey
    private Integer id;

    @NonNull
    @ColumnInfo(name = "number_shares")
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

    @NonNull
    public Integer getId() {
        return id;
    }
}
