package com.doobs.invest.income.repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;

import java.util.List;

/**
 * Created by mduby on 11/25/18.
 */

public class StockHoldingViewModel extends AndroidViewModel {
    // constants
    private String TAG_NAME = this.getClass().getName();

    // instamnce variables
    private StockHoldingRepository stockHoldingRepository;
    private LiveData<PortfolioModel> portfolioModelLiveData;
    private LiveData<List<StockHoldingModel>> stockHoldingList;

    /**
     * default constructor
     *
     * @param application
     */
    public StockHoldingViewModel(@NonNull Application application) {
        super(application);

        // get the repository
        this.stockHoldingRepository = new StockHoldingRepository(application);
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

    public LiveData<List<StockHoldingModel>> getStockHoldingList() {
        return stockHoldingList;
    }
}
