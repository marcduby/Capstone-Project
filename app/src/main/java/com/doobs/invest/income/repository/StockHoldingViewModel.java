package com.doobs.invest.income.repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;
import com.doobs.invest.income.model.StockModel;

import java.util.List;

/**
 * View model class to add a stock holding to a portfolio
 *
 * Created by mduby on 11/25/18.
 */

public class StockHoldingViewModel extends AndroidViewModel {
    // constants
    private String TAG_NAME = this.getClass().getName();

    // instamnce variables
    private StockHoldingRepository stockHoldingRepository;
    private LiveData<PortfolioModel> portfolioModelLiveData;
    private MutableLiveData<String> errorStringMutableLiveData;

    /**
     * default constructor
     *
     * @param application
     */
    public StockHoldingViewModel(@NonNull Application application) {
        super(application);

        // get the repository
        this.stockHoldingRepository = new StockHoldingRepository(application);

        // get the error string live data object
        this.errorStringMutableLiveData = this.stockHoldingRepository.getErrorStringMutableLiveData();
    }

    /**
     * returns the portfolio with the given primary key
     *
     * @param portfolioId
     * @return
     */
    public LiveData<PortfolioModel> getPortfolioModelLiveData(Integer portfolioId) {
        // make sure current portfolio cached is the one we are looking for
        if (this.portfolioModelLiveData == null ||
                this.portfolioModelLiveData.getValue() == null ||
                portfolioId.intValue() != this.portfolioModelLiveData.getValue().getId().intValue()) {

            // log
            Log.i(TAG_NAME, "Refreshing portfolio object with id: " + portfolioId);

            // get the portfolio
            this.portfolioModelLiveData = this.stockHoldingRepository.getPortfolioById(portfolioId);
        }

        // return
        return portfolioModelLiveData;
    }

    /**
     * get the stock portfolio object by id
     *
     * @param portfolioId
     * @return
     */
    public PortfolioModel loadPortfolio(Integer portfolioId) {
        return this.stockHoldingRepository.getPortfolioObjectById(portfolioId);
    }

    public void loadStockHolding(String symbol) {
        this.stockHoldingRepository.findOrCreateStockBySymbol(symbol);
    }

    /**
     * return the stock model
     *
     * @return
     */
    public LiveData<StockModel> getStockModelLiveData() {
        return this.stockHoldingRepository.getStockModelLiveData();
    }

    /**
     * return the stock object from the database
     *
     * @param stockId
     * @return
     */
    public LiveData<StockModel> loadStockModelLiveDataById(Integer stockId) {
        return this.stockHoldingRepository.loadStockModelLiveDataById(stockId);
    }

    /**
     * delete the stock holding object
     *
     * @param stockHoldingModel
     */
    public void deleteStockHolding(StockHoldingModel stockHoldingModel) {
        this.stockHoldingRepository.deleteStockHolding(stockHoldingModel);
    }

    /**
     * load the stock holding object
     *
     * @param stockHoldingId
     * @return
     */
    public LiveData<StockHoldingModel> loadById(Integer stockHoldingId) {
        return this.stockHoldingRepository.loadStockHoldingById(stockHoldingId);
    }

    /**
     * returns the error string live data model
     *
     * @return
     */
    public MutableLiveData<String> getErrorStringMutableLiveData() {
        return errorStringMutableLiveData;
    }

    /**
     * insert or update a stock holding in the persistence layer
     *
     * @param stockHoldingModel
     */
    public void insertOrUpdateStockHolding(StockHoldingModel stockHoldingModel) {
        this.stockHoldingRepository.insertOrUpdateStockHolding(stockHoldingModel);
    }

    /**
     * returns the stock portfolio holdings
     *
     * @param portfolioId
     * @return
     */
    public LiveData<List<StockHoldingModel>> getStockHoldingsForPortfolioId(Integer portfolioId) {
        return this.stockHoldingRepository.getStockHoldingsForPortfolioId(portfolioId);
    }
}
