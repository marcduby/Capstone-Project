package com.doobs.invest.income.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.doobs.invest.income.repository.IncomeViewModel;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeException;

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
    @PrimaryKey(autoGenerate = true)
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

    @Ignore
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

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    /**
     * checks the validity of the portfolio object
     *
     * @throws IncomeException
     */
    public void validityCheck() throws IncomeException {
        // make sure name not null and not empty
        if ((this.name == null) || (this.name.trim().length() < 1)) {
            throw new IncomeException("The portfolio name cannot be empty");
        }

        // make sure descriprion not null and not empty
        if ((this.descriprion == null) || (this.descriprion.trim().length() < 1)) {
            throw new IncomeException("The portfolio descriprion cannot be empty");
        }

        // make sure goal not null and not empty
        if ((this.goal == null) || (this.goal.trim().length() < 1)) {
            throw new IncomeException("The portfolio goal cannot be empty");
        }
    }
}
