package com.doobs.invest.income.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
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
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insert(PortfolioModel portfolioModel);

    @Delete
    public void delete(PortfolioModel portfolioModel);

    @Update
    public void update(PortfolioModel portfolioModel);

    @Query("delete from " + IncomeConstants.Database.TABLE_NAME_PORTFOLIO)
    public void deleteAll();

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_PORTFOLIO + " where id = :id")
    public LiveData<PortfolioModel> loadById(Integer id);

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_PORTFOLIO + " where id = :id")
    public PortfolioModel loadObjectById(Integer id);

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_PORTFOLIO + " order by name")
    public LiveData<List<PortfolioModel>> getAllPortfolios();

    @Query("select * from " + IncomeConstants.Database.TABLE_NAME_PORTFOLIO + " order by name")
    public List<PortfolioModel> getAllPortfolioObjects();

    @Query("select count(id) from " + IncomeConstants.Database.TABLE_NAME_PORTFOLIO + " where name = :name")
    public Integer getCountPortfoliosByName(String name);
}
