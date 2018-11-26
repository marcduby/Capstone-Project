package com.doobs.invest.income.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.doobs.invest.income.dao.PortfolioDao;
import com.doobs.invest.income.dao.StockDao;
import com.doobs.invest.income.dao.StockHoldingDao;
import com.doobs.invest.income.database.IncomeDatabase;
import com.doobs.invest.income.json.StockJsonParser;
import com.doobs.invest.income.json.bean.StockInformationBean;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockModel;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeException;
import com.doobs.invest.income.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Repository class to manage the stock holding objects
 *
 * Created by mduby on 11/25/18.
 */

public class StockHoldingRepository {
    // constants
    private String TAG_NAME = this.getClass().getName();

    // instance variables
    private StockHoldingDao stockHoldingDao;
    private PortfolioDao portfolioDao;
    private StockDao stockDao;
    private MutableLiveData<StockModel> stockModelLiveData = new MutableLiveData<StockModel>();

    public StockHoldingRepository(Application application) {
        // create the database
        IncomeDatabase incomeDatabase = IncomeDatabase.getInstance(application);

        // get the DAOs
        this.stockHoldingDao = incomeDatabase.getStockHoldingDao();
        this.portfolioDao = incomeDatabase.getPortfolioDao();
        this.stockDao = incomeDatabase.getStockDao();
    }

    /**
     * loads the portfolio by id
     *
     * @param portfolioId
     * @return
     */
    public LiveData<PortfolioModel> getPortfolioById(Integer portfolioId) {
        // load the portfolio
        return this.portfolioDao.loadById(portfolioId);
    }

    /**
     * load the portfolio object withput live data
     *
     * @param portfolioId
     * @return
     */
    public PortfolioModel getPortfolioObjectById(Integer portfolioId) {
        // load the portfolio
        return this.portfolioDao.loadObjectById(portfolioId);
    }

    /**
     * get the stock model for the given symbol
     *
     * @param symbol
     * @return
     */
    public LiveData<StockModel> findOrCreateStockBySymbol(String symbol) {
        // only load if not already loaded
        if (this.stockModelLiveData == null ||
                this.stockModelLiveData.getValue() == null ||
                !this.stockModelLiveData.getValue().getSymbol().equals(symbol)) {

            // get the stock from the async task
            new FindOrCreateStockAsyncTask(this.stockDao).execute(symbol);

        } else {
            Log.i(TAG_NAME, "Stock already loaded with symbol: " + symbol);
        }

        // return
        return this.stockModelLiveData;
    }

    /**
     * updates the stock model live data
     *
     * @param stockModel
     */
    public void updateStockModelLiveData(StockModel stockModel) {
        this.stockModelLiveData.postValue(stockModel);
    }

    /**
     * asyc task class to find the stock for a given symbol
     *
     */
    public class FindOrCreateStockAsyncTask extends AsyncTask<String, Void, StockModel> {
        // instance variables
        private StockDao stockDao;

        /**
         * default constructor
         *
         * @param dao
         */
        public FindOrCreateStockAsyncTask(StockDao dao) {
            this.stockDao = dao;
        }

        @Override
        protected StockModel doInBackground(String... symbols) {
            // get the symbol to search with
            String symbol = symbols[0];
//            LiveData<StockModel> stockModelData;
            StockModel stockModel;

            // look for the stock in the database
            stockModel = this.stockDao.getStockBySymbol(symbol);

            // if null, find with REST call
            if (stockModel == null) {
                // log not found
                Log.i(this.getClass().getName(), "Did not find stock in DB with symbol: " + symbol);

                // find with REST
                stockModel = this.findStockFromRestCall(symbol);

                // if not null, save to DB and call again
                if (stockModel != null) {
                    this.stockDao.insert(stockModel);

                    // no do call again
                    stockModel = this.stockDao.getStockBySymbol(symbol);
                }
            }

            // log
            Log.i(this.getClass().getName(), "Found stock id: " + stockModel.getId() + " and name: " + stockModel.getName() + " for symbol: " + symbol);

            // return
            return stockModel;
        }

        @Override
        protected void onPostExecute(StockModel stockModel) {
            updateStockModelLiveData(stockModel);
        }

        /**
         * find a stock through the REST service
         *
         * @param symbol
         * @return
         */
        protected StockModel findStockFromRestCall(String symbol) {
            // TODO - implement
            StockModel stockModel = new StockModel();
            URL stockQueryUrl = null;
            String restCallString = null;
            StockInformationBean stockInformationBean = null;

            // log
            Log.i(this.getClass().getName(), "Calling REST service for symbol: " + symbol);

            try {
                // build the URL
                stockQueryUrl = NetworkUtils.getRestServiceUrl(symbol, IncomeConstants.RestServer.DATA_COMPANY);

                // call the REST service
                restCallString = NetworkUtils.getResponseFromHttpUrl(stockQueryUrl);

                // parse the string to json
                stockInformationBean = StockJsonParser.parseString(restCallString);

                // get the data for the stock model
                stockModel.setSymbol(stockInformationBean.getSymbol());
                stockModel.setName(stockInformationBean.getName());
                stockModel.setIndustry(stockInformationBean.getIndustry());
                stockModel.setIssueType(stockInformationBean.getIssueType());

            } catch (IncomeException exception) {
                // TODO - set live data string error

            } catch (IOException exception) {
                // TODO - set live data string error
            }

            // return
            return stockModel;
        }

        /**
         * find a stock through the REST service
         *
         * @param symbol
         * @return
         */
        protected StockModel findStockFromRestCallTest(String symbol) {
            // TODO - implement
            StockModel stockModel = new StockModel();
            stockModel.setSymbol(symbol);
            stockModel.setName(symbol + " corp");
            stockModel.setIndustry("Misc");
            stockModel.setIssueType("cs");

            // log
            Log.i(this.getClass().getName(), "Calling REST service for symbol: " + symbol);

            // return
            return stockModel;
        }
    }

    public MutableLiveData<StockModel> getStockModelLiveData() {
        return stockModelLiveData;
    }
}
