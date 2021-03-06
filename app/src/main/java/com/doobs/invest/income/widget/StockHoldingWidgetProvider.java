package com.doobs.invest.income.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.doobs.invest.income.R;
import com.doobs.invest.income.dao.PortfolioDao;
import com.doobs.invest.income.database.IncomeDatabase;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.repository.IncomeRepository;
import com.doobs.invest.income.repository.IncomeViewModel;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Random;

/**
 * Widget provider class for the stock holding list display
 *
 * Created by mduby on 11/29/18.
 */

public class StockHoldingWidgetProvider extends AppWidgetProvider {
    // constants
    private String TAG_NAME = this.getClass().getName();
    private static final String ACTION_CLICK = "ACTION_CLICK";

    // local variables
    protected IncomeViewModel incomeViewModel;

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // local variables

        // get the income view model
        IncomeDatabase incomeDatabase = IncomeDatabase.getInstance(context);
        PortfolioDao portfolioDao = incomeDatabase.getPortfolioDao();

        // log
        Log.i(TAG_NAME, "received widget click");

        // get the widget IDs
        ComponentName thisWidget = new ComponentName(context, StockHoldingWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // log firebase event
        // create the bundle
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, IncomeConstants.Firebase.Event.PORTFOLIOS_WIDGET_REFRESH);
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        // call the async task to load the portfolios and update the widget
        new LoadPortfoliosAsyncTask(portfolioDao, context, appWidgetManager, appWidgetIds).execute(1);

//        // for all widgets, update
//        for (int widgetId : allWidgetIds) {
//            // create some random data
//            int number = (new Random().nextInt(100));
//
//            // update
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//            Log.i(TAG_NAME, String.valueOf(number));
//
//            // Set the text
//            remoteViews.setTextViewText(R.id.update, String.valueOf(number) + " dude");
//
//            // Register an onClickListener
//            Intent intent = new Intent(context, StockHoldingWidgetProvider.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.stock_holding_refresh_widget_button, pendingIntent);
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);
//        }

    }

    /**
     * task class to update a portfolio in the DB layer
     *
     */
    public static class LoadPortfoliosAsyncTask extends AsyncTask<Integer, Void, List<PortfolioModel>> {
        // instance variables
        private PortfolioDao portfolioDao;
        private Context context;
        private AppWidgetManager appWidgetManager;
        private int[] appIds;

        /**
         * default constructor
         *
         * @param dao
         */
        public LoadPortfoliosAsyncTask(PortfolioDao dao, Context context, AppWidgetManager appWidegtM, int[] appWidgetIds) {
            this.portfolioDao = dao;
            this.context = context;
            this.appWidgetManager = appWidegtM;
            this.appIds = appWidgetIds;
        }

        @Override
        protected List<PortfolioModel> doInBackground(Integer... integers) {
            // get the list of portfolios
            List<PortfolioModel> portfolioModelList = this.portfolioDao.getAllPortfolioObjects();

            // log
            Log.i(this.getClass().getName(), "Loaded portfolio list of size: " + portfolioModelList.size());

            // return
            return portfolioModelList;
        }

        @Override
        protected void onPostExecute(List<PortfolioModel> portfolioModelList) {
            // calculate the totals
            Double value = 0.0;
            Double dividends = 0.0;
            Double gains = 0.0;
            for (PortfolioModel portfolioModel : portfolioModelList) {
                value = value + portfolioModel.getCurrentValue();
                dividends = dividends + portfolioModel.getTotalDividend();
                gains = gains + portfolioModel.getCurrentValue() - portfolioModel.getCostBasis();
            }

            // for all widgets, update
            for (int widgetId : appIds) {
                // update
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                Log.i(this.getClass().getName(), "Updating widget with id: " + widgetId);

                // Set the text
                remoteViews.setTextViewText(R.id.widget_total_value_textview, IncomeUtils.getCurrencyString(value));
                remoteViews.setTextViewText(R.id.widget_total_dividends_textview, IncomeUtils.getCurrencyString(dividends));
                remoteViews.setTextViewText(R.id.widget_total_gains_textview, IncomeUtils.getCurrencyString(gains));

                // Register an onClickListener
                Intent intent = new Intent(context, StockHoldingWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appIds);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.stock_holding_refresh_widget_button, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    }

}
