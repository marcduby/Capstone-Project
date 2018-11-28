package com.doobs.invest.income;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doobs.invest.income.adapter.PortfolioRecyclerAdapter;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.repository.IncomeViewModel;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PortfolioRecyclerAdapter.PortfolioItemClickListener {
    // constants
    private final String TAG_NAME = this.getClass().getName();

    // instance variables
    private PortfolioRecyclerAdapter portfolioRecyclerAdapter;
    private IncomeViewModel incomeViewModel;
    private LinearLayoutManager portfolioListLayoutManager;

    // widgets
    // recycler view
    @BindView(R.id.portfolio_recyclerview)
    protected RecyclerView portfolioRecyclerView;

    // portfolio value
    @BindView(R.id.portfolio_list_value_textview)
    protected TextView valueTotalTextView;

    // portfolio gain
    @BindView(R.id.portfolio_list_gain_textview)
    protected TextView gainTotalTextView;

    // portfolio dividend
    @BindView(R.id.portfolio_list_dividend_textview)
    protected TextView dividendTotalTextView;

    // FAB
    @BindView(R.id.portfolio_adding_fab)
    protected FloatingActionButton portfolioAddFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind butterknife
        ButterKnife.bind(this);

        // get the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // log
        Log.i(this.TAG_NAME, "In onCreate");

        // get the views
//        this.addPortfolioButton = this.findViewById(R.id.portfolio_add_button);
        this.portfolioAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPortfolio();
            }
        });

        // get the recycler view
//        this.portfolioRecyclerView = (RecyclerView) this.findViewById(R.id.portfolio_recyclerview);
        this.portfolioRecyclerView.setHasFixedSize(true);

        // set the layout manager for the recycler view
        this.portfolioListLayoutManager = new LinearLayoutManager(this);
        this.portfolioRecyclerView.setLayoutManager(this.portfolioListLayoutManager);

        // create the adapter
        this.portfolioRecyclerAdapter = new PortfolioRecyclerAdapter(this);

        // set the adapter on the recycler view
        this.portfolioRecyclerView.setAdapter(this.portfolioRecyclerAdapter);

        // get the income view model
        this.incomeViewModel = ViewModelProviders.of(this).get(IncomeViewModel.class);

        // if there is state saved, reset it
        if (savedInstanceState != null) {
        }

        // set the observer on the database live data portfolio list used for display
        this.incomeViewModel.getPortfolioList().observe(this, new Observer<List<PortfolioModel>>() {
            @Override
            public void onChanged(@Nullable List<PortfolioModel> portfolioModelList) {
                // set the data on the adapter
                portfolioRecyclerAdapter.setPortfolioModelList(portfolioModelList);

                // set the list value totals
                setValueTotals(portfolioModelList);
            }
        });
    }

    /**
     * set the list value totals
     *
     * @param portfolioModelList
     */
    private void setValueTotals(List<PortfolioModel> portfolioModelList) {
        // local variables
        Double value = new Double(0);
        Double gain = new Double(0);
        Double dividend = new Double(0);

        // loop through the list and add up the totals
        if (portfolioModelList != null) {
            for (PortfolioModel portfolioModel : portfolioModelList) {
                if (portfolioModel.getCurrentValue() != null
                        && portfolioModel.getCostBasis() != null
                        && portfolioModel.getTotalDividend() != null) {
                    value = value + portfolioModel.getCurrentValue();
                    gain = gain + portfolioModel.getCurrentValue() - portfolioModel.getCostBasis();
                    dividend = dividend + portfolioModel.getTotalDividend();
                }

            }
        }

        // set the values on the text views
        this.valueTotalTextView.setText(IncomeUtils.getCurrencyString(value));
        this.gainTotalTextView.setText(IncomeUtils.getCurrencyString(gain));
        this.dividendTotalTextView.setText(IncomeUtils.getCurrencyString(dividend));
    }

    /**
     * handles list item clicks
     *
     */
    @Override
    public void onListItemClick(PortfolioModel portfolioModel) {
        // toast
//        Toast.makeText(this, "clicked on item: " + portfolioModel.getName(), Toast.LENGTH_LONG).show();

        // open the stock holding list
        Intent intent = new Intent(this, StockHoldingListActivity.class);
        intent.putExtra(IncomeConstants.ExtraKeys.PORTFOLIO_ID, portfolioModel.getId());
        this.startActivity(intent);
    }

    /**
     * handles the add portfolio option
     *
     */
    private void addPortfolio() {
        // handle the add a portfolio button
//        Toast.makeText(this, this.getString(R.string.adding_portfolio_toast), Toast.LENGTH_LONG).show();

        // create intent and send to new activity
        Intent intent = new Intent(this, PortfolioSavingActivity.class);
        this.startActivity(intent);
    }

}
