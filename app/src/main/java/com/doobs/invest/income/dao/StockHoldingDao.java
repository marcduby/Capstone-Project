package com.doobs.invest.income.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.doobs.invest.income.model.StockHoldingModel;
import com.doobs.invest.income.model.StockModel;
import com.doobs.invest.income.util.IncomeConstants;

import java.util.List;

/**
 * DAO class to manage the stock holding information objects
 *
 * Created by mduby on 11/21/18.
 */
@Dao
public interface StockHoldingDao {
    @Insert
    public void insert(StockHoldingModel stockHoldingModel);

    @Delete
    public void delete(StockHoldingModel stockHoldingModel);

    @Update
    public void update(StockHoldingModel stockHoldingModel);

    @Query("delete from " + IncomeConstants.Database.TABLE_NAME_HOLDING)
    public void deleteAll();

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_HOLDING + " where portfolio_id = :portfolioId")
    public LiveData<List<StockHoldingModel>> getAllStocks(Integer portfolioId);
}
