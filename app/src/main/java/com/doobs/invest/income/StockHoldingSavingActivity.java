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
import com.doobs.invest.income.util.IncomeException;

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

    // stock description
    @BindView(R.id.stock_description_textview)
    protected TextView stockDescriptionTextView;

    // stock symbol
    @BindView(R.id.stock_symbol_textview)
    protected TextView stockSymbolTextView;

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
        setSupportActionBar(toolbar);

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
                updateStockInformation(stockModel);
            }
        });


//        // create the portfolio model
//        this.portfolioModel = new PortfolioModel();

        // get the FAB
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (savePortfolio(portfolioModel)) {
//                    // log
//                    Log.i(TAG_NAME, "Saved portfolio with id: " + portfolioModel.getId() + " and name: " + portfolioModel.getName());
//
//                    // snack bar
//                    Snackbar.make(view, "Portfolio " + portfolioModel.getName() + " saved", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//
//                    // go back
//                    finish();
//                }
//            }
//        });
    }

    protected void searchSymbol() {
        // get the symbol from the input
        String symbol = this.stockHoldingSymbolEditView.getText().toString();

        // check
        if (symbol == null || symbol.trim().length() < 0) {
            Toast.makeText(this, "The symbol needs to be not empty", Toast.LENGTH_LONG);

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

        // set the description
        this.stockDescriptionTextView.setText(this.stockModel.getDescription());
    }

    /**
     * save the stock holding information
     *
     * @return
     */
    private boolean saveStockHolding() {
        // local variables
        boolean saveSuccess = false;
        StockHoldingModel stockHoldingModel = new StockHoldingModel();

        try {
            // get the stock id
            stockHoldingModel.setPortfolioId(this.portfolioModel.getId());

            // set the portfolio id
            stockHoldingModel.setStockId(this.stockModel.getId());

            // set the number of shares
            stockHoldingModel.setNumberOfShares(this.getDoubleFromTextView(this.stockHoldingSymbolEditView, "number of shares"));

            // set the price bought
            stockHoldingModel.setPricePaid(this.getDoubleFromTextView(this.stockHoldingPriceBoughtEditView, "price bought"));

            // make sure all the fields are filled
            stockHoldingModel.validityCheck();

            // save the portfolio
//            this.stockHoldingViewModel.(portfolioModel);
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
            throw new IncomeException("Got incorrect number " + inputString + " for " + type + " field");
        }

        // return
        return value;
    }
}
