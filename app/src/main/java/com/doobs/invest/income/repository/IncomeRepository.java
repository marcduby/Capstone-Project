package com.doobs.invest.income.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import com.doobs.invest.income.dao.PortfolioDao;
import com.doobs.invest.income.dao.StockHoldingDao;
import com.doobs.invest.income.database.IncomeDatabase;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;
import com.doobs.invest.income.model.StockModel;
import com.doobs.invest.income.util.IncomeException;

import java.util.List;
import java.util.Map;

/**
 * Repository class to manage the management of the model objects
 *
 * Created by mduby on 11/22/18.
 */
public class IncomeRepository {
    // instance variables
    private PortfolioDao portfolioDao;
    private StockHoldingDao stockHoldingDao;
    private LiveData<List<PortfolioModel>> portfolioList;
    private Map<Integer, LiveData<List<StockHoldingModel>>> stockHoldingMapByPortfolioId;
    private Map<Integer, LiveData<StockModel>> stockMapByStockId;

    /**
     * default constructors
     *
     * @param application
     */
    public IncomeRepository(Application application) {
        IncomeDatabase incomeDatabase = IncomeDatabase.getInstance(application);
        this.portfolioDao = incomeDatabase.getPortfolioDao();
        this.portfolioList = this.portfolioDao.getAllPortfolios();
    }

    /**
     * returns the portfolio list
     *
     * @return
     */
    public LiveData<List<PortfolioModel>> getPortfolioList() {
        return portfolioList;
    }

    /**
     * inserts a new portfolio object
     *
     * @param portfolioModel
     */
    public void insert(PortfolioModel portfolioModel) {
        new InsertPortfolioAsyncTask(this.portfolioDao).execute(portfolioModel);
    }

    /**
     * returns the portfolio loaded from the database
     *
     * @param portfolioId
     * @return
     */
    public LiveData<PortfolioModel> getPortfolioModelLiveData(Integer portfolioId) {
        return this.portfolioDao.loadById(portfolioId);
    }

    /**
     * deletes a portfolio object
     *
     * @param portfolioModel
     */
    public void delete(PortfolioModel portfolioModel) {
        new DeletePortfolioAsyncTask(this.portfolioDao).execute(portfolioModel);
    }

    /**
     * updates a portfolio object
     *
     * @param portfolioModel
     */
    public void update(PortfolioModel portfolioModel) {
        new UpdatePortfolioAsyncTask(this.portfolioDao).execute(portfolioModel);
    }

    /**
     * get the number of portfolios with the name given
     *
     * @param name
     * @return
     */
    public Integer getCountPortfoliosByName(String name) {
        return this.portfolioDao.getCountPortfoliosByName(name);
    }

    /**
     * async class to insert movie data
     *
     */
    public static class InsertPortfolioAsyncTask extends AsyncTask<PortfolioModel, Void, Void> {
        // instance variables
        private PortfolioDao portfolioDao;

        /**
         * default constructor
         *
         * @param dao
         */
        public InsertPortfolioAsyncTask(PortfolioDao dao) {
            this.portfolioDao = dao;
        }

        @Override
        protected Void doInBackground(PortfolioModel... portfolioModels) {
            // get the portfolio to insert
            PortfolioModel portfolioModel = portfolioModels[0];

            // insert the portfolio
            this.portfolioDao.insert(portfolioModel);

            // log
            Log.i(this.getClass().getName(), "Inserted movie with id: " + portfolioModel.getId() + " and name: " + portfolioModel.getName());

            // return
            return null;
        }
    }

    /**
     * task class to delete a portfolio from the DB layer
     *
     */
    public static class DeletePortfolioAsyncTask extends AsyncTask<PortfolioModel, Void, Void> {
        // instance variables
        private PortfolioDao portfolioDao;

        /**
         * default constructor
         *
         * @param dao
         */
        public DeletePortfolioAsyncTask(PortfolioDao dao) {
            this.portfolioDao = dao;
        }

        @Override
        protected Void doInBackground(PortfolioModel... portfolioModels) {
            // get the portfolio to insert
            PortfolioModel portfolioModel = portfolioModels[0];

            // delete the portfolio
            this.portfolioDao.delete(portfolioModel);

            // log
            Log.i(this.getClass().getName(), "Deleted movie with id: " + portfolioModel.getId() + " and name: " + portfolioModel.getName());

            // return
            return null;
        }
    }

    /**
     * task class to update a portfolio in the DB layer
     *
     */
    public static class UpdatePortfolioAsyncTask extends AsyncTask<PortfolioModel, Void, Void> {
        // instance variables
        private PortfolioDao portfolioDao;

        /**
         * default constructor
         *
         * @param dao
         */
        public UpdatePortfolioAsyncTask(PortfolioDao dao) {
            this.portfolioDao = dao;
        }

        @Override
        protected Void doInBackground(PortfolioModel... portfolioModels) {
            // get the portfolio to insert
            PortfolioModel portfolioModel = portfolioModels[0];

            // update the portfolio
            this.portfolioDao.update(portfolioModel);

            // log
            Log.i(this.getClass().getName(), "Updated movie with id: " + portfolioModel.getId() + " and name: " + portfolioModel.getName());

            // return
            return null;
        }
    }

}
