package com.doobs.invest.income.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.doobs.invest.income.R;

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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // local variables

        // log
        Log.i(TAG_NAME, "received widget click");

        // get the widget IDs
        ComponentName thisWidget = new ComponentName(context, StockHoldingWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // for all widgets, update
        for (int widgetId : allWidgetIds) {
            // create some random data
            int number = (new Random().nextInt(100));

            // update
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Log.i(TAG_NAME, String.valueOf(number));

            // Set the text
            remoteViews.setTextViewText(R.id.update, String.valueOf(number) + " dude");

            // Register an onClickListener
            Intent intent = new Intent(context, StockHoldingWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.stock_holding_refresh_widget_button, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
