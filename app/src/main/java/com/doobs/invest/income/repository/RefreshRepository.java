package com.doobs.invest.income.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

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
 * Repository class to refresh the market data
 *
 * Created by mduby on 11/28/18.
 */

public class RefreshRepository {
    // constants
    private String TAG_NAME = this.getClass().getName();

    // instance variables
    private PortfolioDao portfolioDao;
    private StockHoldingDao stockHoldingDao;
    private StockDao stockDao;

    /**
     * default constructor
     *
     * @param application
     */
    public RefreshRepository(Application application) {
        // create the database
        IncomeDatabase incomeDatabase = IncomeDatabase.getInstance(application);

        // get the DAOs
        this.stockHoldingDao = incomeDatabase.getStockHoldingDao();
        this.portfolioDao = incomeDatabase.getPortfolioDao();
        this.stockDao = incomeDatabase.getStockDao();
    }

    public void refreshAllPortfolios() {
        // local variables

        // refresh the stocks

        // get all portfolios

        // refresh the stock holdings

        // refresh the portfolios

    }

    /**
     * refresh a portfolio
     *
     * @param portfolioId
     */
    public void refreshPortfolio(Integer portfolioId) {
        // log
        Log.i(TAG_NAME, "Refreshing prices for portfolio: " + portfolioId);
        new UpdatePortfolioAsyncTask(this.stockDao, this.stockHoldingDao, this.portfolioDao).execute(portfolioId);
    }

    /**
     * class to update stocks
     *
     */
    public class UpdatePortfolioAsyncTask extends AsyncTask<Integer, Void, Void> {
        // instance variables
        private StockDao stockDao;
        private StockHoldingDao stockHoldingDao;
        private PortfolioDao portfolioDao;

        /**
         * default constructor
         *
         */
        public UpdatePortfolioAsyncTask(StockDao sDao, StockHoldingDao shDao, PortfolioDao pDao) {
            this.stockHoldingDao = shDao;
            this.stockDao = sDao;
            this.portfolioDao = pDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            // get the symbol to search with
            Integer portfolioId = integers[0];
            List<StockModel> stockModelList;
            List<StockHoldingModel> stockHoldingModelList;
            PortfolioModel portfolioModel;

            // load all stocks for this portfolio
            stockModelList = this.stockDao.getStockByPortfolioId(portfolioId);

            try {
                // test network
                NetworkUtils.testNetwork();

                // loop to update the stock information
                for (StockModel stockModel : stockModelList) {
                    // log
                    Log.i(TAG_NAME, "Updating stock price for stock symbol: " + stockModel.getSymbol());

                    // update the stock
                    NetworkUtils.updateStockFromRestCall(stockModel);

                    // set industry as default if none
                    if (stockModel.getIndustry() == null || stockModel.getIndustry().trim().length() < 1) {
                        stockModel.setIndustry(IncomeConstants.RestCodes.Industry.DEFAULT);
                    }

                    // update the stock model
                    this.stockDao.update(stockModel);
                }

                // get the stock holding information for the portfolio
                stockHoldingModelList = this.stockHoldingDao.getAllStocksObjects(portfolioId);

                // loop through the stock holdings and recalculate based on new stock price
                for (StockHoldingModel stockHoldingModel : stockHoldingModelList) {
                    // get the stock for the stock holding
                    StockModel stockModel = this.stockDao.getStockById(stockHoldingModel.getStockId());

                    // update the numbers
                    stockHoldingModel.setCurrentValue(stockHoldingModel.getNumberOfShares() * stockModel.getPrice());
                    stockHoldingModel.setTotalDividend(stockHoldingModel.getNumberOfShares() * stockModel.getDividend());

                    // save
                    this.stockHoldingDao.update(stockHoldingModel);
                }

                // load the portfolio model
                portfolioModel = this.portfolioDao.loadObjectById(portfolioId);
                Log.i(TAG_NAME, "Current value of portfolio name: " + portfolioModel.getName() + " is: " + portfolioModel.getCurrentValue());

                // update the portfolio
                IncomeUtils.updatePortfolioTotals(stockHoldingModelList, portfolioModel);
                Log.i(TAG_NAME, "New value of portfolio name: " + portfolioModel.getName() + " is: " + portfolioModel.getCurrentValue());

                // save the portfolio
                this.portfolioDao.update(portfolioModel);

            } catch (IncomeException exception) {
                // log error, set error string and return null
                Log.e(TAG_NAME, "Got network error:  " + exception.getMessage());
            }

            // return
            return null;
        }

        /**
         * find a stock through the REST service
         *
         * @param stockModel
         * @return
         */
        protected void updateStockFromRestCall(StockModel stockModel) {
            // TODO - implement
            URL stockQueryUrl = null;
            String restCallString = null;
            StockInformationBean stockInformationBean = null;
            StockQuoteBean stockQuoteBean = null;
            StockStatsBean stockStatsBean = null;

            // log
            Log.i(this.getClass().getName(), "Calling REST service for symbol: " + stockModel.getSymbol() + " and stock id: " + stockModel.getId());

            try {
                // get the stock information
                // build the URL
                stockQueryUrl = NetworkUtils.getRestServiceUrl(stockModel.getSymbol(), IncomeConstants.RestServer.DATA_COMPANY);

                // call the REST service
                restCallString = NetworkUtils.getResponseFromHttpUrl(stockQueryUrl);

                // parse the string to json
                stockInformationBean = StockJsonParser.getStockInformationFromJsonString(restCallString);

                // get the data for the stock model
                stockModel.setName(stockInformationBean.getName());
                stockModel.setIndustry(stockInformationBean.getIndustry());
                stockModel.setIssueType(stockInformationBean.getIssueType());

                // get the stock price
                // build the URL
                stockQueryUrl = NetworkUtils.getRestServiceUrl(stockModel.getSymbol(), IncomeConstants.RestServer.DATA_QUOTE);

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
                stockQueryUrl = NetworkUtils.getRestServiceUrl(stockModel.getSymbol(), IncomeConstants.RestServer.DATA_STATS);

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

            } catch (IOException exception) {
                // log
                Log.e(TAG_NAME, "Got IO error parsing the REST call: " + exception.getMessage());
            }
        }
    }

}
