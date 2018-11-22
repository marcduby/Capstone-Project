package com.doobs.invest.income.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.util.IncomeConstants;

import java.util.List;

/**
 * DAO class to manage the portfolio model objects
 *
 * Created by mduby on 11/21/18.
 */
@Dao
public interface PortfolioDao {
    @Insert
    public void insert(PortfolioModel portfolioModel);

    @Delete
    public void delete(PortfolioModel portfolioModel);

    @Update
    public void update(PortfolioModel portfolioModel);

    @Query("delete from " + IncomeConstants.Database.TABLE_NAME_PORTFOLIO)
    public void deleteAll();

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_PORTFOLIO + " order by name")
    public LiveData<List<PortfolioModel>> getAllPortfolios();

}
