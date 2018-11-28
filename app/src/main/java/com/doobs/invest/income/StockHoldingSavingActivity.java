package com.doobs.invest.income;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.doobs.invest.income.repository.IncomeViewModel;
import com.doobs.invest.income.repository.StockHoldingViewModel;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeException;
import com.doobs.invest.income.util.IncomeUtils;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockHoldingSavingActivity extends AppCompatActivity {
    // constants
    private final String TAG_NAME = this.getClass().getName();

    // instance variables
    private StockHoldingModel stockHoldingModel;
    private StockHoldingViewModel stockHoldingViewModel;
    private StockModel stockModel;
    private PortfolioModel portfolioModel;

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

    // symbol search button
    @BindView(R.id.symbol_search_button)
    protected Button symbolSearchButton;

    // stock symbol search view
    @BindView(R.id.stock_holding_symbol_editview)
    protected EditText stockHoldingSymbolEditView;

    // stock number shares search view
    @BindView(R.id.stock_holding_number_shares_editview)
    protected EditText stockHoldingNumberSharestEditView;

    // stock price bought search view
    @BindView(R.id.stock_holding_price_editview)
    protected EditText stockHoldingPriceBoughtEditView;

    // FAB
    @BindView(R.id.stock_holding_saving_fab)
    protected FloatingActionButton stockHoldingSavingFab;

    /**
     * onCreate method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_holding_saving);

        // bind butterknife
        ButterKnife.bind(this);

        // get the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get the income view model
        this.stockHoldingViewModel = ViewModelProviders.of(this).get(StockHoldingViewModel.class);

        // add a listener to the button
        this.symbolSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchSymbol();
            }

        });

        // observe the stock live data
        this.stockHoldingViewModel.getStockModelLiveData().observe(this, new Observer<StockModel>() {
            @Override
            public void onChanged(@Nullable StockModel stockModel) {
                // call the view update method
                if (stockModel != null) {
                    updateStockInformation(stockModel);
                }
            }
        });


//        // load the portfolio model
        final Integer portfolioId = getIntent().getIntExtra(IncomeConstants.ExtraKeys.PORTFOLIO_ID, 0);
        this.stockHoldingViewModel.getPortfolioModelLiveData(portfolioId).observe(this, new Observer<PortfolioModel>() {
            @Override
            public void onChanged(@Nullable PortfolioModel model) {
                portfolioModel = model;
            }
        });

        // get the FAB
        stockHoldingSavingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveStockHolding()) {
                    // log
                    Log.i(TAG_NAME, "Saved stock holding for portfolio with id: " + portfolioModel.getId() + " and stock: " + stockModel.getName());

                    // snack bar
                    Snackbar.make(view, "Stock holding " + stockHoldingModel.getId() + " saved", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    // go back
                    finish();
                }
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
     * show the error string
     *
     * @param errorString
     */
    protected void showErrorString(String errorString) {
        Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
    }

    protected void searchSymbol() {
        // get the symbol from the input
        String symbol = this.stockHoldingSymbolEditView.getText().toString().toUpperCase();

        // check
        if (symbol == null || symbol.trim().length() < 1) {
            Toast.makeText(this, this.getString(R.string.empty_stock_symbol_error), Toast.LENGTH_LONG).show();

        } else {
            this.stockHoldingViewModel.loadStockHolding(symbol);
        }
    }

    /**
     * sets the stock information in the UI
     *
     * @param model
     */
    protected void updateStockInformation(StockModel model) {
        // set the instance
        this.stockModel = model;

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

    /**
     * save the stock holding information
     *
     * @return
     */
    private boolean saveStockHolding() {
        // local variables
        boolean saveSuccess = false;
        StockHoldingModel newStockHoldingModel = new StockHoldingModel();

        if (this.stockModel == null) {
            this.showErrorString(this.getString(R.string.not_found_stock_symbol_error));

        } else {
            // create the stock holding
            try {
                // get the stock id
                newStockHoldingModel.setPortfolioId(this.portfolioModel.getId());

                // set the portfolio id
                newStockHoldingModel.setStockId(this.stockModel.getId());

                // set the number of shares
                newStockHoldingModel.setNumberOfShares(this.getDoubleFromTextView(this.stockHoldingNumberSharestEditView, "number of shares"));

                // set the price bought
                newStockHoldingModel.setPricePaid(this.getDoubleFromTextView(this.stockHoldingPriceBoughtEditView, "price bought"));

                // set the stock sybol
                newStockHoldingModel.setStockSymbol(this.stockModel.getSymbol());

                // set the industry of the holding
                String industry = this.stockModel.getIndustry();
                newStockHoldingModel.setIndustry(industry);

                // set the current value
                newStockHoldingModel.setCurrentValue(newStockHoldingModel.getNumberOfShares() * this.stockModel.getPrice());

                // set the total dividend
                newStockHoldingModel.setTotalDividend(newStockHoldingModel.getNumberOfShares() * this.stockModel.getDividend());

                // make sure all the fields are filled
                newStockHoldingModel.validityCheck();

                // save the portfolio
                this.stockHoldingViewModel.insertOrUpdateStockHolding(newStockHoldingModel);
                saveSuccess = true;

            } catch (IncomeException exception) {
                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }

            // assign the stock holding model
            this.stockHoldingModel = newStockHoldingModel;
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
