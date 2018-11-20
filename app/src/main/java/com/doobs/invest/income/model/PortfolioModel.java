package com.doobs.invest.income.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.doobs.invest.income.util.IncomeConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class to hold stock portfolio information
 *
 * Created by mduby on 11/16/18.
 */
@Entity(tableName = IncomeConstants.Database.TABLE_NAME_PORTFOLIO)
public class PortfolioModel {
    // local variables
    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey
    private Integer id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "description")
    private String descriprion;

    @NonNull
    @ColumnInfo(name = "goal")
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

    @NonNull
    public Integer getId() {
        return id;
    }
}
