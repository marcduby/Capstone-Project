package com.doobs.invest.income.repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;

import java.util.List;

/**
 * View model clas for the income tracker application
 *
 * Created by mduby on 11/22/18.
 */

public class IncomeViewModel extends AndroidViewModel {
    // instance variables
    private IncomeRepository incomeRepository;
    private LiveData<List<PortfolioModel>> portfolioList;

    /**
     * default constructor
     *
     * @param application
     */
    public IncomeViewModel(@NonNull Application application) {
        super(application);

        // build the repository
        this.incomeRepository = new IncomeRepository(application);

        // get the portfolio list
        this.portfolioList = this.incomeRepository.getPortfolioList();
    }

    /**
     * inserts a portfilio in the persistence layer
     *
     * @param portfolioModel
     */
    public void insertPortfolio(PortfolioModel portfolioModel) {
        this.incomeRepository.insert(portfolioModel);
    }

    /**
     * updates a portfolio
     *
     * @param portfolioModel
     */
    public void updatePortfolio(PortfolioModel portfolioModel) {
        this.incomeRepository.update(portfolioModel);
    }

    /**
     * deletes a portfolio
     *
     * @param portfolioModel
     */
    public void deletePortfolio(PortfolioModel portfolioModel) {
        this.incomeRepository.delete(portfolioModel);
    }

    /**
     * count the portfolios with the given name
     *
     * @param name
     * @return
     */
    public Integer getCountPortfoliosByNamwe(String name) {
        return this.incomeRepository.getCountPortfoliosByName(name);
    }

    /**
     * get the portfoio list
     *
     * @return
     */
    public LiveData<List<PortfolioModel>> getPortfolioList() {
        return portfolioList;
    }

}
