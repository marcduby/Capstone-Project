package com.doobs.invest.income;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;
import com.doobs.invest.income.model.StockModel;
import com.doobs.invest.income.repository.StockHoldingViewModel;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeException;
import com.doobs.invest.income.util.IncomeUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockHoldingUpdatingActivity extends AppCompatActivity {
    // constants
    private final String TAG_NAME = this.getClass().getName();

    // instance variables
    private StockHoldingModel stockHoldingModel;
    private StockModel stockModel;
    private FirebaseAnalytics firebaseAnalytics;
    private StockHoldingViewModel stockHoldingViewModel;

    // widgets
    // stock name text view
    @BindView(R.id.stock_name_textview)
    protected TextView stockNameTextView;

    // stock industry
    @BindView(R.id.stock_industry_textview)
    protected TextView stockIndustryTextView;

    // stock symbol
    @BindView(R.id.stock_symbol_textview)
    protected TextView stockSymbolTextView;

    // stock issue type
    @BindView(R.id.stock_issue_type_textview)
    protected TextView stockIssueTypeTextView;

    // stock price
    @BindView(R.id.stock_price_textview)
    protected TextView stockPriceTextView;

    // stock dividend
    @BindView(R.id.stock_dividend_textview)
    protected TextView stockDividendTextView;

    // stock yield
    @BindView(R.id.stock_yield_textview)
    protected TextView stockYieldTextView;

    // stock number shares search view
    @BindView(R.id.stock_holding_number_shares_editview)
    protected EditText stockHoldingNumberSharestEditView;

    // stock price bought search view
    @BindView(R.id.stock_holding_price_editview)
    protected EditText stockHoldingPriceBoughtEditView;

    // FAB
    @BindView(R.id.stock_holding_saving_fab)
    protected FloatingActionButton stockHoldingSavingFab;

    // FAB
    @BindView(R.id.stock_holding_deleting_fab)
    protected FloatingActionButton stockHoldingDeletingFab;

    /**
     * onCreate method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_holding_updating);

        // bind butterknife
        ButterKnife.bind(this);

        // get the firebase analytics
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // get the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get the stock holding model id
        Intent intent = this.getIntent();
        final Integer stockHoldingId = intent.getIntExtra(IncomeConstants.ExtraKeys.STOCK_HOLDING_ID, 0);
        final Integer stockId = intent.getIntExtra(IncomeConstants.ExtraKeys.STOCK_ID, 0);
        Log.i(TAG_NAME, "Loaded stock holding with id: " + stockHoldingId + " and stock with id: " + stockId);

        // get the income view model
        this.stockHoldingViewModel = ViewModelProviders.of(this).get(StockHoldingViewModel.class);

        // observe the stock live data
        this.stockHoldingViewModel.loadStockModelLiveDataById(stockId).observe(this, new Observer<StockModel>() {
            @Override
            public void onChanged(@Nullable StockModel model) {
                stockModel = model;

                // load the data in the views
                updateStockHoldingInformation();
            }
        });


        // load the stock holding
        this.stockHoldingViewModel.loadById(stockHoldingId).observe(this, new Observer<StockHoldingModel>() {
            @Override
            public void onChanged(@Nullable StockHoldingModel model) {
                // set the stock holding model
                stockHoldingModel = model;

                // load the data in the views
                updateStockHoldingInformation();
            }
        });

        // get the updating FAB
        stockHoldingSavingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveStockHolding()) {
                    // log
                    Log.i(TAG_NAME, "Saved stock holding with id: " + stockHoldingModel.getId() + " and stock: " + stockHoldingModel.getStockSymbol());

                    // go back
                    finish();
                }
            }
        });

        // get the deleting FAB
        stockHoldingDeletingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG_NAME, "Deleting stock holding with id: " + stockHoldingModel.getId() + " and stock: " + stockHoldingModel.getStockSymbol());

                // delete the stock holding object
                deleteStockHolding(stockHoldingModel);

                // go back
                finish();
            }
        });

        // add listener to the error message
        this.stockHoldingViewModel.getErrorStringMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String errorMessageString) {
                if (errorMessageString != null && errorMessageString.trim().length() > 0) {
                    showErrorString(errorMessageString);
                }
            }
        });
    }

    /**
     * delete the stock holding model
     *
     * @param stockHoldingModel
     */
    private void deleteStockHolding(StockHoldingModel stockHoldingModel) {
        // log the firebase event
        IncomeUtils.logFirebaseEvent(this.firebaseAnalytics, IncomeConstants.Firebase.Event.STOCK_HOLDING_DELETE);

        // delete the stock holding
        this.stockHoldingViewModel.deleteStockHolding(stockHoldingModel);
    }

    /**
     * show the error string
     *
     * @param errorString
     */
    protected void showErrorString(String errorString) {
        Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
    }

    /**
     * sets the stock information in the UI
     *
     */
    protected void updateStockHoldingInformation() {
        if (this.stockHoldingModel != null) {
            // for the stock holding
            // set the share number
            this.stockHoldingNumberSharestEditView.setText(this.stockHoldingModel.getNumberOfShares().toString());

            // set the price bought
            this.stockHoldingPriceBoughtEditView.setText(this.stockHoldingModel.getPricePaid().toString());
        }

        if (this.stockModel != null) {
            // for the stock
            // set the name
            this.stockNameTextView.setText(this.stockModel.getName());

            // set the symbol
            this.stockSymbolTextView.setText(this.stockModel.getSymbol());

            // set the industry
            this.stockIndustryTextView.setText(this.stockModel.getIndustry());

            // set the issue type
            this.stockIssueTypeTextView.setText(this.stockModel.getIssueTypeDescription());

            // set the price
            this.stockPriceTextView.setText(IncomeUtils.getCurrencyString(this.stockModel.getPrice()));

            // set the yield
            this.stockYieldTextView.setText(IncomeUtils.getPercentString(this.stockModel.getYield()));

            // set the dividend
            this.stockDividendTextView.setText(IncomeUtils.getCurrencyString(this.stockModel.getDividend()));
        }

        // log the firebase event
        IncomeUtils.logFirebaseEvent(this.firebaseAnalytics, IncomeConstants.Firebase.Event.STOCK_HOLDING_UPDATE);
    }

    /**
     * save the stock holding information
     *
     * @return
     */
    private boolean saveStockHolding() {
        // local variables
        boolean saveSuccess = false;

        // create the stock holding
        try {

            // set the number of shares
            this.stockHoldingModel.setNumberOfShares(this.getDoubleFromTextView(this.stockHoldingNumberSharestEditView, "number of shares"));

            // set the price bought
            this.stockHoldingModel.setPricePaid(this.getDoubleFromTextView(this.stockHoldingPriceBoughtEditView, "price bought"));

            // set the new value
            this.stockHoldingModel.setCurrentValue(this.stockHoldingModel.getNumberOfShares() * this.stockModel.getPrice());

            // set the new dividend
            this.stockHoldingModel.setTotalDividend(this.stockHoldingModel.getNumberOfShares() * this.stockModel.getDividend());

            // make sure all the fields are filled
            this.stockHoldingModel.validityCheck();

            // save the portfolio
            this.stockHoldingViewModel.insertOrUpdateStockHolding(this.stockHoldingModel);
            saveSuccess = true;

        } catch (IncomeException exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        // return
        return saveSuccess;
    }

    /**
     * get the double number from the text field
     *
     * @param textView
     * @param type
     * @return
     * @throws IncomeException
     */
    protected Double getDoubleFromTextView(EditText textView, String type) throws IncomeException {
        // local variables
        String inputString = null;
        Double value = null;

        // get the string from the text view
        inputString = textView.getText().toString();

        // convert to a double
        try {
            value = Double.valueOf(inputString);

        } catch (NumberFormatException exception) {
            Log.i(TAG_NAME, "Got number format exception: " + exception.getMessage());
            throw new IncomeException("Got incorrect number " + inputString + " for " + type + " field");
        }

        // return
        return value;
    }
}
