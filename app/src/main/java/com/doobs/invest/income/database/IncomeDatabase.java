package com.doobs.invest.income.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.doobs.invest.income.dao.PortfolioDao;
import com.doobs.invest.income.dao.StockDao;
import com.doobs.invest.income.dao.StockHoldingDao;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;
import com.doobs.invest.income.model.StockModel;
import com.doobs.invest.income.util.IncomeConstants;

/**
 * Romm database class
 *
 * Created by mduby on 11/21/18.
 */
@Database(entities = {PortfolioModel.class, StockHoldingModel.class, StockModel.class}, version = 1, exportSchema = false)
public abstract class IncomeDatabase extends RoomDatabase {
    // static variables
    private static IncomeDatabase incomeDatabase;

    /**
     * singleton method to return the movie database
     *
     * @param context
     * @return
     */
    public static IncomeDatabase getInstance(final Context context) {
        if (incomeDatabase == null) {
            synchronized (IncomeDatabase.class) {
                incomeDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        IncomeDatabase.class,
                        IncomeConstants.Database.DATABASE_NAME).build();
            }
        }

        // return
        return incomeDatabase;
    }

    /**
     * returns the stock dao
     *
     * @return
     */
    public abstract StockDao getStockDao();

    /**
     * returns the portfolio dao
     *
     * @return
     */
    public abstract PortfolioDao getPortfolioDao();

    /**
     * returns the stock holding dao
     *
     * @return
     */
    public abstract StockHoldingDao getStockHoldingDao();
}
