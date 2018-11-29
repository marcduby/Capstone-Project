package com.doobs.invest.income.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockModel;
import com.doobs.invest.income.util.IncomeConstants;

import java.util.List;

/**
 * DAO class to manage the stock information objects
 *
 * Created by mduby on 11/21/18.
 */
@Dao
public interface StockDao {
    @Insert
    public void insert(StockModel stockModel);

    @Delete
    public void delete(StockModel stockModel);

    @Update
    public void update(StockModel stockModel);

    @Query("delete from " + IncomeConstants.Database.TABLE_NAME_STOCK_INFORMATION)
    public void deleteAll();

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_STOCK_INFORMATION + " order by symbol")
    public LiveData<List<StockModel>> getAllStocks();

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_STOCK_INFORMATION + " where id = :stockId")
    public StockModel getStockById(Integer stockId);

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_STOCK_INFORMATION + " where id = :stockId")
    public LiveData<StockModel> getStockLiveDataById(Integer stockId);

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_STOCK_INFORMATION + " where symbol = :symbol")
    public StockModel getStockBySymbol(String symbol);

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_STOCK_INFORMATION + " where id = :stockId")
    public LiveData<StockModel> getStock(Integer stockId);
}
