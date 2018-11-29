package com.doobs.invest.income.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.doobs.invest.income.R;
import com.doobs.invest.income.dao.PortfolioDao;
import com.doobs.invest.income.dao.StockDao;
import com.doobs.invest.income.dao.StockHoldingDao;
import com.doobs.invest.income.database.IncomeDatabase;
import com.doobs.invest.income.json.StockJsonParser;
import com.doobs.invest.income.json.bean.StockInformationBean;
import com.doobs.invest.income.json.bean.StockQuoteBean;
import com.doobs.invest.income.json.bean.StockStatsBean;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;
import com.doobs.invest.income.model.StockModel;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeException;
import com.doobs.invest.income.util.IncomeUtils;
import com.doobs.invest.income.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
    private MutableLiveData<String> errorStringMutableLiveData = new MutableLiveData<String>();

    public StockHoldingRepository(Application application) {
        // create the database
        IncomeDatabase incomeDatabase = IncomeDatabase.getInstance(application);

        // get the DAOs
        this.stockHoldingDao = incomeDatabase.getStockHoldingDao();
        this.portfolioDao = incomeDatabase.getPortfolioDao();
        this.stockDao = incomeDatabase.getStockDao();
    }

    /**
     * insert a stock holding in the DB
     *
     * @param stockHoldingModel
     */
    public void insertOrUpdateStockHolding(StockHoldingModel stockHoldingModel) {
        Log.i(TAG_NAME, "Saving stock hilding with id: " + stockHoldingModel.getId());

        // insert/update the stock holding
        new InsertOrUpdateStockHoldingAsyncTask(this.stockHoldingDao, this.portfolioDao).execute(stockHoldingModel);
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
     * returns the stock holdings for a portfolio
     *
     * @param portfolioId
     * @return
     */
    public LiveData<List<StockHoldingModel>> getStockHoldingsForPortfolioId(Integer portfolioId) {
        // get the list of stock holdings
        LiveData<List<StockHoldingModel>> listLiveData = this.stockHoldingDao.getAllStocks(portfolioId);

        // return
        return listLiveData;
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
     * returns the error string live data object
     *
     * @return
     */
    public MutableLiveData<String> getErrorStringMutableLiveData() {
        return errorStringMutableLiveData;
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
     * return the stock object from the database
     *
     * @param stockId
     * @return
     */
    public LiveData<StockModel> loadStockModelLiveDataById(Integer stockId) {
        return this.stockDao.getStockLiveDataById(stockId);
    }

    /**
     * delete the stock holding object
     *
     * @param stockHoldingModel
     */
    public void deleteStockHolding(StockHoldingModel stockHoldingModel) {
        // get the portfolio id
        Integer portfolioId = stockHoldingModel.getPortfolioId();

        // delete
        new DeleteStockHoldingAsyncTask(this.stockHoldingDao).execute(stockHoldingModel);

        // update the portfolio totals
        this.updatePortfolioTotals(portfolioId);
    }

    /**
     * update the portfolio totals
     *
     * @param portfolioId
     */
    public void updatePortfolioTotals(Integer portfolioId) {
        // update the portfolio totals
        new UpdatePortfolioTotalsAsyncTask(this.stockHoldingDao, this.portfolioDao).execute(portfolioId);
    }

    /**
     * load the stock holding object
     *
     * @param stockHoldingId
     * @return
     */
    public LiveData<StockHoldingModel> loadStockHoldingById(Integer stockHoldingId) {
        return this.stockHoldingDao.loadById(stockHoldingId);
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
            StockModel stockModel;

            // look for the stock in the database
            stockModel = this.stockDao.getStockBySymbol(symbol);

            // if null, find with REST call
            if (stockModel == null) {
                // log not found
                Log.i(this.getClass().getName(), "Did not find stock in DB with symbol: " + symbol);

                // test network
                try {
                    NetworkUtils.testNetwork();

                } catch (IncomeException exception) {
                    // log error, set error string and return null
                    Log.e(TAG_NAME, "Got network error:  " + exception.getMessage());
//                    errorStringMutableLiveData.postValue(Resources.getSystem().getString(R.string.no_network_error));
                    errorStringMutableLiveData.postValue(IncomeConstants.ErrorCodes.NO_NETWORK);
                    return null;
                }

                // find with REST
                stockModel = this.findStockFromRestCall(symbol);

                // if null symbol, error
                if (stockModel == null) {
                    // log error, set error string and return null
                    Log.e(TAG_NAME, "Got incorrect stock symbol search for symbol:  " + symbol);
//                    errorStringMutableLiveData.postValue(Resources.getSystem().getString(R.string.no_stock_symbol_error));
                    errorStringMutableLiveData.postValue(IncomeConstants.ErrorCodes.INCORRECT_STOCK_SYMBOL);
                    return null;
                }

                // clear out the error string
                errorStringMutableLiveData.postValue(null);

                // set industry as default if none
                if (stockModel.getIndustry() == null || stockModel.getIndustry().trim().length() < 1) {
//                    stockModel.setIndustry(Resources.getSystem().getString(R.string.multiple_industry_value));
                    stockModel.setIndustry(IncomeConstants.RestCodes.Industry.DEFAULT);
                }

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
            StockQuoteBean stockQuoteBean = null;
            StockStatsBean stockStatsBean = null;

            // log
            Log.i(this.getClass().getName(), "Calling REST service for symbol: " + symbol);

            try {
                // get the stock information
                // build the URL
                stockQueryUrl = NetworkUtils.getRestServiceUrl(symbol, IncomeConstants.RestServer.DATA_COMPANY);

                // call the REST service
                restCallString = NetworkUtils.getResponseFromHttpUrl(stockQueryUrl);

                // parse the string to json
                stockInformationBean = StockJsonParser.getStockInformationFromJsonString(restCallString);

                // get the data for the stock model
                stockModel.setSymbol(stockInformationBean.getSymbol());
                stockModel.setName(stockInformationBean.getName());
                stockModel.setIndustry(stockInformationBean.getIndustry());
                stockModel.setIssueType(stockInformationBean.getIssueType());

                // get the stock price
                // build the URL
                stockQueryUrl = NetworkUtils.getRestServiceUrl(symbol, IncomeConstants.RestServer.DATA_QUOTE);

                // call the REST service
                restCallString = NetworkUtils.getResponseFromHttpUrl(stockQueryUrl);

                // parse the string to json
                stockQuoteBean = StockJsonParser.getStockQuoteFromJsonString(restCallString);

                // get the data for the stock model
                stockModel.setPrice(stockQuoteBean.getPrice());
                stockModel.setPeRatio(stockQuoteBean.getPeRatio());
                stockModel.setPriceChange(stockQuoteBean.getPriceChange());
                stockModel.setDateString(IncomeUtils.getCurrentDateString());

                // get the stock stats
                // build the URL
                stockQueryUrl = NetworkUtils.getRestServiceUrl(symbol, IncomeConstants.RestServer.DATA_STATS);

                // call the REST service
                restCallString = NetworkUtils.getResponseFromHttpUrl(stockQueryUrl);

                // parse the string to json
                stockStatsBean = StockJsonParser.getStockStatsFromJsonString(restCallString);

                // get the data for the stock model
                stockModel.setDividend(stockStatsBean.getDividend());
                stockModel.setYield(stockStatsBean.getYield());
                stockModel.setBeta(stockStatsBean.getBeta());

            } catch (IncomeException exception) {
                // log
                Log.e(TAG_NAME, "Got error parsing the REST call: " + exception.getMessage());

                // TODO - set live data string error
                stockModel = null;

            } catch (IOException exception) {
                // log
                Log.e(TAG_NAME, "Got IO error parsing the REST call: " + exception.getMessage());

                // TODO - set live data string error
                stockModel = null;
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

    /**
     * Asyc task class to delete a stock holding
     *
     */
    public static class DeleteStockHoldingAsyncTask extends AsyncTask<StockHoldingModel, Void, Void> {
        // instance variables
        private StockHoldingDao stockHoldingDao;

        /**
         * default constructor
         *
         * @param shDao
         */
        public DeleteStockHoldingAsyncTask(StockHoldingDao shDao) {
            this.stockHoldingDao = shDao;
        }

        @Override
        protected Void doInBackground(StockHoldingModel... stockHoldingModels) {
            // get the portfolio to delete
            StockHoldingModel stockHoldingModel = stockHoldingModels[0];

            // log
            Log.i(this.getClass().getName(), "Deleting stock holding with id: " + stockHoldingModel.getId() + " and stock: " + stockHoldingModel.getStockSymbol());

            // delete the stock holding
            this.stockHoldingDao.delete(stockHoldingModel);

            // return
            return null;
        }
    }

    /**
     * async task class to insert or update a stock holding
     *
     */
    public static class InsertOrUpdateStockHoldingAsyncTask extends AsyncTask<StockHoldingModel, Void, Void> {
        // instance variables
        private StockHoldingDao stockHoldingDao;
        private PortfolioDao portfolioDao;

        /**
         * default constructor
         *
         * @param shDao
         * @param pDao
         */
        public InsertOrUpdateStockHoldingAsyncTask(StockHoldingDao shDao, PortfolioDao pDao) {
            this.stockHoldingDao = shDao;
            this.portfolioDao = pDao;
        }

        @Override
        protected Void doInBackground(StockHoldingModel... stockHoldingModels) {
            // get the stock holding to insert
            StockHoldingModel stockHoldingModel = stockHoldingModels[0];

            // insert the stock holding
            if (stockHoldingModel.getId() == null) {
                this.stockHoldingDao.insert(stockHoldingModel);

            } else {
                this.stockHoldingDao.update(stockHoldingModel);
            }

            // update the portfolio totals as well
            this.updatePortfolioTotals(stockHoldingModel.getPortfolioId());

            // log
            Log.i(this.getClass().getName(), "Inserted movie with id: " + stockHoldingModel.getId() + " and stock id: " + stockHoldingModel.getStockId());

            // return
            return null;
        }

        /**
         * updates the portfolio model
         *
         */
        protected void updatePortfolioTotals(Integer portfolioId) {
            // local variables
            PortfolioModel portfolioModel;
            List<StockHoldingModel> stockHoldingModels;
            Double cost = new Double(0);
            Double value = new Double(0);
            Double dividend = new Double(0);

            // load the portfolio model
            portfolioModel = this.portfolioDao.loadObjectById(portfolioId);

            // load the list of stock holdings for the portfolio
            stockHoldingModels = this.stockHoldingDao.getAllStocksObjects(portfolioId);

            // add up the amounts
            for (StockHoldingModel stockHoldingModel : stockHoldingModels) {
                cost = cost + stockHoldingModel.getCostBasis();
                value = value + stockHoldingModel.getCurrentValue();
                dividend = dividend + stockHoldingModel.getTotalDividend();
            }
            portfolioModel.setCostBasis(cost);
            portfolioModel.setCurrentValue(value);
            portfolioModel.setTotalDividend(dividend);

            // save the portfolio model
            this.portfolioDao.update(portfolioModel);
        }
    }

    /**
     * Async task class to update the portfolio totals
     *
     */
    public static class UpdatePortfolioTotalsAsyncTask extends AsyncTask<Integer, Void, Void> {
        // instance variables
        private StockHoldingDao stockHoldingDao;
        private PortfolioDao portfolioDao;

        /**
         * default constructor
         *
         * @param shDao
         * @param pDao
         */
        public UpdatePortfolioTotalsAsyncTask(StockHoldingDao shDao, PortfolioDao pDao) {
            this.stockHoldingDao = shDao;
            this.portfolioDao = pDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            // local variables
            PortfolioModel portfolioModel;
            List<StockHoldingModel> stockHoldingModels;
            Double cost = new Double(0);
            Double value = new Double(0);
            Double dividend = new Double(0);

            // get the stock holding to insert
            Integer portfolioId = integers[0];

            // load the portfolio model
            portfolioModel = this.portfolioDao.loadObjectById(portfolioId);

            // load the list of stock holdings for the portfolio
            stockHoldingModels = this.stockHoldingDao.getAllStocksObjects(portfolioId);

            // add up the amounts
            for (StockHoldingModel stockHoldingModel : stockHoldingModels) {
                cost = cost + stockHoldingModel.getCostBasis();
                value = value + stockHoldingModel.getCurrentValue();
                dividend = dividend + stockHoldingModel.getTotalDividend();
            }
            portfolioModel.setCostBasis(cost);
            portfolioModel.setCurrentValue(value);
            portfolioModel.setTotalDividend(dividend);

            // save the portfolio model
            this.portfolioDao.update(portfolioModel);

            // log
            Log.i(this.getClass().getName(), "Updated the totals for portfolio with id: " + portfolioModel.getId() + " and name: " + portfolioModel.getName());

            // return
            return null;
        }
    }

}
