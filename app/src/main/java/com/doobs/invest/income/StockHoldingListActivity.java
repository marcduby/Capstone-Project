package com.doobs.invest.income;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doobs.invest.income.adapter.PortfolioRecyclerAdapter;
import com.doobs.invest.income.adapter.StockHoldingRecyclerAdapter;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;
import com.doobs.invest.income.repository.IncomeViewModel;
import com.doobs.invest.income.repository.StockHoldingViewModel;
import com.doobs.invest.income.util.IncomeConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * activity class to display stock holdings of a portfolio
 *
 */
public class StockHoldingListActivity extends AppCompatActivity implements StockHoldingRecyclerAdapter.StockHoldingItemClickListener {
    // constants
    private final String TAG_NAME = this.getClass().getName();

    // instance variables
    private StockHoldingRecyclerAdapter stockHoldingRecyclerAdapter;
    private RecyclerView stockHoldingRecyclerView;
    private StockHoldingViewModel stockHoldingViewModel;
    private LinearLayoutManager stockHoldingListLayoutManager;
    private PortfolioModel portfolioModel;

    // widgets
    @BindView(R.id.portfolio_name_textview)
    protected TextView portfolioNameTextView;

    @BindView(R.id.stock_holding_add_button)
    protected Button addStockHoldingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_holding_list);

        // bind butterknife
        ButterKnife.bind(this);

        // get the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // log
        Log.i(this.TAG_NAME, "In onCreate");

        // get the views
        this.addStockHoldingButton = this.findViewById(R.id.stock_holding_add_button);
        this.addStockHoldingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStockHolding();
            }
        });

        // get the recycler view
        this.stockHoldingRecyclerView = (RecyclerView) this.findViewById(R.id.stock_holding_recyclerview);
        this.stockHoldingRecyclerView.setHasFixedSize(true);

        // set the layout manager for the recycler view
        this.stockHoldingListLayoutManager = new LinearLayoutManager(this);
        this.stockHoldingRecyclerView.setLayoutManager(this.stockHoldingListLayoutManager);

        // create the adapter
        this.stockHoldingRecyclerAdapter = new StockHoldingRecyclerAdapter(this);

        // set the adapter on the recycler view
        this.stockHoldingRecyclerView.setAdapter(this.stockHoldingRecyclerAdapter);

        // get the income view model
        this.stockHoldingViewModel = ViewModelProviders.of(this).get(StockHoldingViewModel.class);

        // load the portfolio from the intent
        Integer portfolioId = this.getIntent().getIntExtra(IncomeConstants.ExtraKeys.PORTFOLIO_ID, 0);
        this.stockHoldingViewModel.getPortfolioModelLiveData(portfolioId).observe(this, new Observer<PortfolioModel>() {
            @Override
            public void onChanged(@Nullable PortfolioModel portfolioModelReturn) {
                portfolioModel = portfolioModelReturn;
                portfolioNameTextView.setText(portfolioModel.getName() + getString(R.string.portfolio_snippet));
            }
        });
//        this.portfolioModel = this.stockHoldingViewModel.loadPortfolio(portfolioId);

        // if there is state saved, reset it
        if (savedInstanceState != null) {
        }

        // set the observer on the database live data portfolio list used for display
        this.stockHoldingViewModel.getStockHoldingsForPortfolioId(portfolioId).observe(this, new Observer<List<StockHoldingModel>>() {
            @Override
            public void onChanged(@Nullable List<StockHoldingModel> stockHoldingModelList) {
                stockHoldingRecyclerAdapter.setStockHoldingModelList(stockHoldingModelList);
            }
        });
    }

    /**
     * handles list item clicks
     *
     */
    @Override
    public void onListItemClick(StockHoldingModel stockHoldingModel) {
        Toast.makeText(this, "clicked on item: " + stockHoldingModel.getDescription(), Toast.LENGTH_LONG).show();
    }

    /**
     * handles the add portfolio option
     *
     */
    private void addStockHolding() {
        // handle the add a portfolio button
        Toast.makeText(this, "adding stock holding", Toast.LENGTH_LONG).show();

        // call the view model
//        PortfolioModel portfolioModel = new PortfolioModel();
//        portfolioModel.setName("portfolio: " + new Date().getTime());
//        portfolioModel.setGoal("goal test");
//        portfolioModel.setDescriprion("test decsrition");
//        this.incomeViewModel.insertPortfolio(portfolioModel);

        // create intent and send to new activity
        Intent intent = new Intent(this, StockHoldingSavingActivity.class);
        intent.putExtra(IncomeConstants.ExtraKeys.PORTFOLIO_ID, this.portfolioModel.getId());
        this.startActivity(intent);
    }

}
